package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.network.GeminiClient
import com.example.tts.VoiceCoachManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

enum class ActiveWorkoutState {
    IDLE,
    EXERCISING,
    RESTING,
    COMPLETED
}

class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FitPulseDatabase.getDatabase(application, viewModelScope)
    private val repository = FitPulseRepository(db.dao())
    val voiceCoach = VoiceCoachManager(application)

    private val moshi = Moshi.Builder().build()
    private val routineExercisesType = Types.newParameterizedType(List::class.java, RoutineExercise::class.java)
    private val routineExercisesAdapter = moshi.adapter<List<RoutineExercise>>(routineExercisesType)

    // Current Date formatting
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val _selectedDate = MutableStateFlow(dateFormat.format(Date()))
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    // User Profile
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PrepopulatedData.defaultProfile)

    // Daily Meal Logs for selected date
    val todayMealLogs: StateFlow<List<MealLog>> = _selectedDate.flatMapLatest { date ->
        repository.getMealLogsForDate(date)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Food Database
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val foodItems: StateFlow<List<FoodItem>> = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) repository.allFoods else repository.searchFoods(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Exercises
    val allExercises: StateFlow<List<Exercise>> = repository.allExercises
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Routines
    val routines: StateFlow<List<WorkoutRoutine>> = repository.allRoutines
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Workout History
    val workoutHistory: StateFlow<List<WorkoutHistory>> = repository.workoutHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Weekly Meal Plan
    val weeklyMealPlan: StateFlow<List<WeeklyMealPlanItem>> = repository.weeklyMealPlan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Coach Chat
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Weight Logs
    val weightLogs: StateFlow<List<WeightLog>> = repository.weightLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- AI Food Scanner State ---
    private val _isScanningFood = MutableStateFlow(false)
    val isScanningFood: StateFlow<Boolean> = _isScanningFood.asStateFlow()

    private val _scannedFoodResult = MutableStateFlow<FoodItem?>(null)
    val scannedFoodResult: StateFlow<FoodItem?> = _scannedFoodResult.asStateFlow()

    private val _scanError = MutableStateFlow<String?>(null)
    val scanError: StateFlow<String?> = _scanError.asStateFlow()

    // --- AI Nutritionist State ---
    private val _isGeneratingMealPlan = MutableStateFlow(false)
    val isGeneratingMealPlan: StateFlow<Boolean> = _isGeneratingMealPlan.asStateFlow()

    private val _nutritionistMessage = MutableStateFlow<String?>(null)
    val nutritionistMessage: StateFlow<String?> = _nutritionistMessage.asStateFlow()

    // --- AI Coach Chat Sending State ---
    private val _isCoachThinking = MutableStateFlow(false)
    val isCoachThinking: StateFlow<Boolean> = _isCoachThinking.asStateFlow()

    // --- ACTIVE WORKOUT STATE ---
    private val _activeRoutine = MutableStateFlow<WorkoutRoutine?>(null)
    val activeRoutine: StateFlow<WorkoutRoutine?> = _activeRoutine.asStateFlow()

    private val _activeExercisesList = MutableStateFlow<List<RoutineExercise>>(emptyList())
    val activeExercisesList: StateFlow<List<RoutineExercise>> = _activeExercisesList.asStateFlow()

    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex.asStateFlow()

    private val _currentSetIndex = MutableStateFlow(1)
    val currentSetIndex: StateFlow<Int> = _currentSetIndex.asStateFlow()

    private val _workoutState = MutableStateFlow(ActiveWorkoutState.IDLE)
    val workoutState: StateFlow<ActiveWorkoutState> = _workoutState.asStateFlow()

    private val _timerSecondsRemaining = MutableStateFlow(0)
    val timerSecondsRemaining: StateFlow<Int> = _timerSecondsRemaining.asStateFlow()

    private val _totalWorkoutSecondsElapsed = MutableStateFlow(0)
    val totalWorkoutSecondsElapsed: StateFlow<Int> = _totalWorkoutSecondsElapsed.asStateFlow()

    private var workoutTimerJob: Job? = null
    private var countdownTimerJob: Job? = null

    init {
        // Start check if database needs seed on first launch or missing history
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repository.getUserProfileOnce()
            if (profile == null) {
                repository.updateProfile(PrepopulatedData.defaultProfile)
                repository.setWeeklyMealPlan(PrepopulatedData.starterWeeklyMealPlan)
            }

            // Ensure full 30-day historical + present day mock data is present
            if (repository.getMealLogCount() < 10) {
                repository.addMealLogs(PrepopulatedData.generatePastMonthMealLogs())
            }
            if (repository.getWorkoutHistoryCount() < 5) {
                repository.addWorkoutHistories(PrepopulatedData.generatePastMonthWorkoutHistory())
            }
            if (repository.getWeightLogCount() < 5) {
                repository.addWeightLogs(PrepopulatedData.generatePastMonthWeightLogs())
            }
            if (repository.getChatMessageCount() == 0) {
                repository.addChatMessages(PrepopulatedData.starterChatMessages)
            }
        }
    }

    fun refreshHistoricalData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMealLogs(PrepopulatedData.generatePastMonthMealLogs())
            repository.addWorkoutHistories(PrepopulatedData.generatePastMonthWorkoutHistory())
            repository.addWeightLogs(PrepopulatedData.generatePastMonthWeightLogs())
        }
    }

    // --- Date Navigation ---
    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun shiftDate(days: Int) {
        try {
            val cal = Calendar.getInstance()
            cal.time = dateFormat.parse(_selectedDate.value) ?: Date()
            cal.add(Calendar.DAY_OF_YEAR, days)
            _selectedDate.value = dateFormat.format(cal.time)
        } catch (e: Exception) {
            _selectedDate.value = dateFormat.format(Date())
        }
    }

    // --- Meal Logging ---
    fun logMeal(
        mealType: String,
        foodItem: FoodItem,
        quantity: Double,
        servingUnit: String = foodItem.servingUnit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val ratio = if (foodItem.servingSize > 0) quantity / foodItem.servingSize else 1.0
            val mealLog = MealLog(
                date = _selectedDate.value,
                mealType = mealType,
                foodItemId = foodItem.id,
                foodName = foodItem.name,
                quantity = quantity,
                servingUnit = servingUnit,
                calories = (foodItem.calories * ratio).coerceAtLeast(0.0),
                protein = (foodItem.protein * ratio).coerceAtLeast(0.0),
                carbs = (foodItem.carbs * ratio).coerceAtLeast(0.0),
                fats = (foodItem.fats * ratio).coerceAtLeast(0.0)
            )
            repository.addMealLog(mealLog)
        }
    }

    fun quickLogFromMealPlan(item: WeeklyMealPlanItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val mealLog = MealLog(
                date = _selectedDate.value,
                mealType = item.mealType.uppercase(),
                foodName = item.title,
                quantity = 1.0,
                servingUnit = "serving",
                calories = item.calories.toDouble(),
                protein = item.protein,
                carbs = item.carbs,
                fats = item.fats
            )
            repository.addMealLog(mealLog)
            repository.setMealPlanItemLogged(item.id, true)
        }
    }

    fun deleteMealLog(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMealLog(id)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // --- Custom Recipe & Food Creation ---
    fun addCustomFoodItem(
        name: String,
        brand: String,
        category: String,
        servingSize: Double,
        servingUnit: String,
        calories: Double,
        protein: Double,
        carbs: Double,
        fats: Double,
        fiber: Double = 0.0,
        isCustomRecipe: Boolean = false,
        recipeIngredients: String = ""
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = FoodItem(
                name = name,
                brand = brand.ifBlank { "Custom" },
                category = category,
                servingSize = servingSize,
                servingUnit = servingUnit,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fats = fats,
                fiber = fiber,
                isCustomRecipe = isCustomRecipe,
                recipeIngredients = recipeIngredients
            )
            repository.addFoodItem(item)
        }
    }

    // --- AI Food Scanner (Vision / Text) ---
    fun scanFoodWithAi(bitmap: Bitmap?, textDescription: String?) {
        _isScanningFood.value = true
        _scanError.value = null
        _scannedFoodResult.value = null

        viewModelScope.launch(Dispatchers.IO) {
            val prompt = """
                Analyze this food item, dish, meal plate, or nutrition label:
                "${textDescription ?: "Food in image"}"
                
                Respond ONLY with a valid JSON object in this exact schema:
                {
                  "name": "Concise food name",
                  "category": "Protein / Carbs / Fats / Fruits & Veggies / Dairy & Eggs / Meals & Recipes / Snacks",
                  "servingSize": 100.0,
                  "servingUnit": "g",
                  "calories": 250.0,
                  "protein": 20.0,
                  "carbs": 30.0,
                  "fats": 5.0,
                  "fiber": 3.0,
                  "confidence": "high/medium",
                  "ingredients": "Key ingredients summary"
                }
            """.trimIndent()

            val result = if (bitmap != null) {
                GeminiClient.analyzeImage(bitmap, prompt)
            } else {
                GeminiClient.generateText(prompt, "You are an expert sports nutritionist and nutrition analyzer. Output strictly valid JSON.")
            }

            result.onSuccess { rawResponse ->
                try {
                    val cleanJson = extractJson(rawResponse)
                    val jsonObj = JSONObject(cleanJson)
                    val foodItem = FoodItem(
                        name = jsonObj.optString("name", "Scanned Food"),
                        brand = "AI SmartScan",
                        category = jsonObj.optString("category", "Meals & Recipes"),
                        servingSize = jsonObj.optDouble("servingSize", 100.0),
                        servingUnit = jsonObj.optString("servingUnit", "g"),
                        calories = jsonObj.optDouble("calories", 0.0),
                        protein = jsonObj.optDouble("protein", 0.0),
                        carbs = jsonObj.optDouble("carbs", 0.0),
                        fats = jsonObj.optDouble("fats", 0.0),
                        fiber = jsonObj.optDouble("fiber", 0.0),
                        isCustomRecipe = true,
                        recipeIngredients = jsonObj.optString("ingredients", "")
                    )
                    _scannedFoodResult.value = foodItem
                } catch (e: Exception) {
                    _scanError.value = "Failed to parse nutrition data. Please retry or enter manually."
                }
            }.onFailure { error ->
                _scanError.value = error.message ?: "Failed to connect to AI Nutritionist."
            }

            _isScanningFood.value = false
        }
    }

    fun clearScannedResult() {
        _scannedFoodResult.value = null
        _scanError.value = null
    }

    // --- Profile & Goal Updating ---
    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(profile)
        }
    }

    fun logWeight(weightKg: Double, notes: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            val weightLog = WeightLog(
                date = _selectedDate.value,
                weightKg = weightKg,
                notes = notes
            )
            repository.logWeight(weightLog)
            // Also update current weight on profile
            userProfile.value?.let { current ->
                repository.updateProfile(current.copy(weightKg = weightKg))
            }
        }
    }

    // --- Routine Maker & Management ---
    fun saveRoutine(
        id: Long = 0,
        name: String,
        description: String,
        dayOfWeek: String,
        targetDurationMinutes: Int,
        exercises: List<RoutineExercise>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val json = routineExercisesAdapter.toJson(exercises)
            val routine = WorkoutRoutine(
                id = id,
                name = name,
                description = description,
                dayOfWeek = dayOfWeek,
                targetDurationMinutes = targetDurationMinutes,
                exercisesJson = json
            )
            if (id == 0L) {
                repository.addRoutine(routine)
            } else {
                repository.updateRoutine(routine)
            }
        }
    }

    fun deleteRoutine(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRoutine(id)
        }
    }

    fun parseRoutineExercises(routine: WorkoutRoutine): List<RoutineExercise> {
        return try {
            routineExercisesAdapter.fromJson(routine.exercisesJson) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // --- ACTIVE WORKOUT SESSION & VOICE COACH ---
    fun startWorkout(routine: WorkoutRoutine) {
        _activeRoutine.value = routine
        val exList = parseRoutineExercises(routine)
        _activeExercisesList.value = exList
        _currentExerciseIndex.value = 0
        _currentSetIndex.value = 1
        _workoutState.value = ActiveWorkoutState.EXERCISING
        _totalWorkoutSecondsElapsed.value = 0

        // Start elapsed timer
        workoutTimerJob?.cancel()
        workoutTimerJob = viewModelScope.launch {
            while (_workoutState.value != ActiveWorkoutState.COMPLETED && _workoutState.value != ActiveWorkoutState.IDLE) {
                delay(1000)
                _totalWorkoutSecondsElapsed.value += 1
            }
        }

        // Voice announcement
        val firstEx = exList.firstOrNull()
        if (firstEx != null) {
            voiceCoach.speak("Starting ${routine.name}. First exercise: ${firstEx.exerciseName}. Set 1 of ${firstEx.targetSets}. Let's get it!")
            startExerciseTimer(firstEx)
        }
    }

    private fun startExerciseTimer(exercise: RoutineExercise) {
        countdownTimerJob?.cancel()
        if (exercise.targetDurationSeconds > 0) {
            _timerSecondsRemaining.value = exercise.targetDurationSeconds
            countdownTimerJob = viewModelScope.launch {
                while (_timerSecondsRemaining.value > 0 && _workoutState.value == ActiveWorkoutState.EXERCISING) {
                    delay(1000)
                    _timerSecondsRemaining.value -= 1
                    if (_timerSecondsRemaining.value in 1..5) {
                        voiceCoach.speakCountdown(_timerSecondsRemaining.value)
                    }
                }
                if (_workoutState.value == ActiveWorkoutState.EXERCISING && _timerSecondsRemaining.value <= 0) {
                    completeCurrentSet()
                }
            }
        } else {
            _timerSecondsRemaining.value = 0
        }
    }

    fun completeCurrentSet() {
        val exList = _activeExercisesList.value
        val currentIndex = _currentExerciseIndex.value
        val currentEx = exList.getOrNull(currentIndex) ?: return
        val currentSet = _currentSetIndex.value

        countdownTimerJob?.cancel()

        if (currentSet < currentEx.targetSets) {
            // Move to rest timer
            _workoutState.value = ActiveWorkoutState.RESTING
            val restSeconds = currentEx.restTimeSeconds.coerceAtLeast(30)
            _timerSecondsRemaining.value = restSeconds
            voiceCoach.speak("Set $currentSet complete! Rest for $restSeconds seconds. Breathe and hydrate.")

            countdownTimerJob = viewModelScope.launch {
                while (_timerSecondsRemaining.value > 0 && _workoutState.value == ActiveWorkoutState.RESTING) {
                    delay(1000)
                    _timerSecondsRemaining.value -= 1
                    if (_timerSecondsRemaining.value == 10) {
                        voiceCoach.speak("10 seconds remaining, get into position!")
                    } else if (_timerSecondsRemaining.value in 1..3) {
                        voiceCoach.speakCountdown(_timerSecondsRemaining.value)
                    }
                }
                if (_workoutState.value == ActiveWorkoutState.RESTING) {
                    _currentSetIndex.value += 1
                    _workoutState.value = ActiveWorkoutState.EXERCISING
                    voiceCoach.speak("Set ${_currentSetIndex.value} of ${currentEx.exerciseName}. Let's go!")
                    startExerciseTimer(currentEx)
                }
            }
        } else {
            // Exercise completed, move to next exercise or finish
            if (currentIndex < exList.size - 1) {
                _currentExerciseIndex.value += 1
                _currentSetIndex.value = 1
                val nextEx = exList[_currentExerciseIndex.value]
                _workoutState.value = ActiveWorkoutState.RESTING
                val restSeconds = 60
                _timerSecondsRemaining.value = restSeconds
                voiceCoach.speak("${currentEx.exerciseName} completed! Awesome work. Up next: ${nextEx.exerciseName}. Rest for $restSeconds seconds.")

                countdownTimerJob = viewModelScope.launch {
                    while (_timerSecondsRemaining.value > 0 && _workoutState.value == ActiveWorkoutState.RESTING) {
                        delay(1000)
                        _timerSecondsRemaining.value -= 1
                        if (_timerSecondsRemaining.value == 10) {
                            voiceCoach.speak("10 seconds! Get ready for ${nextEx.exerciseName}.")
                        } else if (_timerSecondsRemaining.value in 1..3) {
                            voiceCoach.speakCountdown(_timerSecondsRemaining.value)
                        }
                    }
                    if (_workoutState.value == ActiveWorkoutState.RESTING) {
                        _workoutState.value = ActiveWorkoutState.EXERCISING
                        voiceCoach.speak("Starting Set 1 of ${nextEx.exerciseName}!")
                        startExerciseTimer(nextEx)
                    }
                }
            } else {
                finishWorkout()
            }
        }
    }

    fun skipRestTimer() {
        countdownTimerJob?.cancel()
        val exList = _activeExercisesList.value
        val currentIndex = _currentExerciseIndex.value
        val currentEx = exList.getOrNull(currentIndex) ?: return

        _workoutState.value = ActiveWorkoutState.EXERCISING
        voiceCoach.speak("Rest skipped. Let's get right into the set!")
        startExerciseTimer(currentEx)
    }

    fun finishWorkout() {
        countdownTimerJob?.cancel()
        workoutTimerJob?.cancel()
        _workoutState.value = ActiveWorkoutState.COMPLETED

        val routine = _activeRoutine.value
        val exList = _activeExercisesList.value
        val totalSecs = _totalWorkoutSecondsElapsed.value

        // Calculate total volume and estimated calories
        var totalVolume = 0.0
        exList.forEach { ex ->
            totalVolume += (ex.weightKg * ex.targetReps * ex.targetSets)
        }
        val estimatedCalories = (totalSecs / 60.0) * 8.5 // ~8.5 kcal per min of weight training

        voiceCoach.speak("Workout complete! Outstanding job crushing your session today! You lifted ${totalVolume.toInt()} total kg over ${totalSecs / 60} minutes.")

        viewModelScope.launch(Dispatchers.IO) {
            val history = WorkoutHistory(
                date = _selectedDate.value,
                routineId = routine?.id,
                routineName = routine?.name ?: "Custom Routine",
                durationSeconds = totalSecs,
                totalVolumeKg = totalVolume,
                caloriesBurned = estimatedCalories,
                completedExercisesCount = exList.size
            )
            repository.logWorkout(history)
        }
    }

    fun cancelActiveWorkout() {
        countdownTimerJob?.cancel()
        workoutTimerJob?.cancel()
        voiceCoach.stop()
        _workoutState.value = ActiveWorkoutState.IDLE
        _activeRoutine.value = null
    }

    // --- AI NUTRITIONIST WEEKLY PLAN GENERATOR ---
    fun generateWeeklyMealPlan(userDietaryNotes: String? = null) {
        _isGeneratingMealPlan.value = true
        _nutritionistMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            val profile = userProfile.value ?: PrepopulatedData.defaultProfile
            val prompt = """
                Create a complete 7-day personalized meal plan (Monday to Sunday) for this user profile:
                - Name: ${profile.name}, Age: ${profile.age}, Gender: ${profile.gender}
                - Weight: ${profile.weightKg} kg, Target Weight: ${profile.targetWeightKg} kg, Height: ${profile.heightCm} cm
                - Goal: ${profile.goal}
                - Daily Targets: ${profile.dailyCalorieTarget} kcal, ${profile.proteinTargetGrams}g Protein, ${profile.carbsTargetGrams}g Carbs, ${profile.fatsTargetGrams}g Fats
                - Dietary Preference: ${profile.dietaryPreference}
                - Medical / Dietary Notes: ${profile.medicalNotes} ${userDietaryNotes ?: ""}
                
                For EACH day of the week (Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday), provide 4 meals:
                1. Breakfast
                2. Lunch
                3. Dinner
                4. Snack
                
                Output ONLY a JSON Array containing 28 meal items (4 meals x 7 days) in this exact schema:
                [
                  {
                    "dayOfWeek": "Monday",
                    "mealType": "Breakfast",
                    "title": "Power Berry Protein Oatmeal",
                    "description": "Short culinary description and prep method",
                    "ingredients": "Rolled Oats 50g, Whey Protein 30g, Chia Seeds 10g, Blueberries 60g, Almond Milk 200ml",
                    "calories": 430,
                    "protein": 34.0,
                    "carbs": 52.0,
                    "fats": 9.0
                  }
                ]
            """.trimIndent()

            val result = GeminiClient.generateText(prompt, "You are a world-class registered dietitian, sports nutritionist, and culinary chef. Return strictly valid JSON array.")

            result.onSuccess { rawResponse ->
                try {
                    val cleanJson = extractJson(rawResponse)
                    val jsonArray = JSONArray(cleanJson)
                    val items = mutableListOf<WeeklyMealPlanItem>()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        items.add(
                            WeeklyMealPlanItem(
                                dayOfWeek = obj.optString("dayOfWeek", "Monday"),
                                mealType = obj.optString("mealType", "Breakfast"),
                                title = obj.optString("title", "Nutritious Meal"),
                                description = obj.optString("description", ""),
                                ingredients = obj.optString("ingredients", ""),
                                calories = obj.optInt("calories", 400),
                                protein = obj.optDouble("protein", 30.0),
                                carbs = obj.optDouble("carbs", 40.0),
                                fats = obj.optDouble("fats", 10.0),
                                isLogged = false
                            )
                        )
                    }
                    if (items.isNotEmpty()) {
                        repository.setWeeklyMealPlan(items)
                        _nutritionistMessage.value = "New 7-Day AI Meal Plan generated successfully for ${profile.goal}!"
                    }
                } catch (e: Exception) {
                    _nutritionistMessage.value = "AI plan format error. Reverted to default template."
                }
            }.onFailure { error ->
                _nutritionistMessage.value = error.message ?: "Failed to generate AI meal plan."
            }

            _isGeneratingMealPlan.value = false
        }
    }

    // --- AI FITNESS COACH CHAT ---
    fun sendCoachMessage(userText: String) {
        if (userText.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            // Save user message
            val userMsg = ChatMessage(role = "user", content = userText)
            repository.addChatMessage(userMsg)
            _isCoachThinking.value = true

            val profile = userProfile.value ?: PrepopulatedData.defaultProfile
            val todayLogs = todayMealLogs.value
            val totalCal = todayLogs.sumOf { it.calories }
            val totalProtein = todayLogs.sumOf { it.protein }
            val totalCarbs = todayLogs.sumOf { it.carbs }
            val totalFats = todayLogs.sumOf { it.fats }

            val systemInstruction = """
                You are Coach Alex, an elite AI Strength, Conditioning, and Nutrition Mentor inside FitPulse.
                User profile context:
                - Name: ${profile.name}, Age: ${profile.age}, Gender: ${profile.gender}
                - Weight: ${profile.weightKg} kg (Target: ${profile.targetWeightKg} kg)
                - Goal: ${profile.goal}, Split: ${profile.workoutSplit}, Equipment: ${profile.equipment}
                - Daily Macro Target: ${profile.dailyCalorieTarget} kcal (${profile.proteinTargetGrams}g P / ${profile.carbsTargetGrams}g C / ${profile.fatsTargetGrams}g F)
                - Today's Consumed So Far: ${totalCal.toInt()} kcal (${totalProtein.toInt()}g P / ${totalCarbs.toInt()}g C / ${totalFats.toInt()}g F)
                - Health/Medical notes: ${profile.medicalNotes}
                
                Respond in an energetic, knowledgeable, scientifically grounded, and motivating tone. Give concise, actionable bullet points when appropriate.
            """.trimIndent()

            val result = GeminiClient.generateText(userText, systemInstruction)

            result.onSuccess { responseText ->
                repository.addChatMessage(ChatMessage(role = "coach", content = responseText))
            }.onFailure { error ->
                repository.addChatMessage(
                    ChatMessage(
                        role = "coach",
                        content = "I had trouble reaching the training server (${error.message}). Remember to keep pushing toward your ${profile.goal} goal today!"
                    )
                )
            }

            _isCoachThinking.value = false
        }
    }

    fun clearCoachChat() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearChat()
        }
    }

    private fun extractJson(text: String): String {
        val trimmed = text.trim()
        val startBrace = trimmed.indexOf('{')
        val startBracket = trimmed.indexOf('[')

        val startIndex = when {
            startBrace >= 0 && startBracket >= 0 -> minOf(startBrace, startBracket)
            startBrace >= 0 -> startBrace
            startBracket >= 0 -> startBracket
            else -> 0
        }

        val endBrace = trimmed.lastIndexOf('}')
        val endBracket = trimmed.lastIndexOf(']')
        val endIndex = maxOf(endBrace, endBracket)

        return if (startIndex in 0 until endIndex) {
            trimmed.substring(startIndex, endIndex + 1)
        } else {
            trimmed
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceCoach.shutdown()
        workoutTimerJob?.cancel()
        countdownTimerJob?.cancel()
    }
}
