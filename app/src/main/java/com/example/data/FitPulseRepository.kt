package com.example.data

import kotlinx.coroutines.flow.Flow

class FitPulseRepository(private val dao: FitPulseDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    suspend fun getUserProfileOnce(): UserProfile? = dao.getUserProfileOnce()
    suspend fun updateProfile(profile: UserProfile) = dao.insertOrUpdateProfile(profile)

    val allFoods: Flow<List<FoodItem>> = dao.getAllFoodItems()
    fun searchFoods(query: String): Flow<List<FoodItem>> = dao.searchFoodItems(query)
    suspend fun addFoodItem(foodItem: FoodItem): Long = dao.insertFoodItem(foodItem)
    suspend fun deleteFoodItem(id: Long) = dao.deleteFoodItem(id)

    fun getMealLogsForDate(date: String): Flow<List<MealLog>> = dao.getMealLogsForDate(date)
    suspend fun addMealLog(mealLog: MealLog): Long = dao.insertMealLog(mealLog)
    suspend fun addMealLogs(mealLogs: List<MealLog>) = dao.insertMealLogs(mealLogs)
    suspend fun getMealLogCount(): Int = dao.getMealLogCount()
    suspend fun deleteMealLog(id: Long) = dao.deleteMealLog(id)

    val allExercises: Flow<List<Exercise>> = dao.getAllExercises()
    fun getExercisesByCategory(category: String): Flow<List<Exercise>> = dao.getExercisesByCategory(category)
    suspend fun getExerciseById(id: String): Exercise? = dao.getExerciseById(id)

    val allRoutines: Flow<List<WorkoutRoutine>> = dao.getAllRoutines()
    suspend fun getRoutineById(id: Long): WorkoutRoutine? = dao.getRoutineById(id)
    suspend fun addRoutine(routine: WorkoutRoutine): Long = dao.insertRoutine(routine)
    suspend fun updateRoutine(routine: WorkoutRoutine) = dao.updateRoutine(routine)
    suspend fun deleteRoutine(id: Long) = dao.deleteRoutine(id)

    val workoutHistory: Flow<List<WorkoutHistory>> = dao.getAllWorkoutHistory()
    suspend fun logWorkout(history: WorkoutHistory): Long = dao.insertWorkoutHistory(history)
    suspend fun addWorkoutHistories(histories: List<WorkoutHistory>) = dao.insertWorkoutHistories(histories)
    suspend fun getWorkoutHistoryCount(): Int = dao.getWorkoutHistoryCount()

    val weeklyMealPlan: Flow<List<WeeklyMealPlanItem>> = dao.getWeeklyMealPlan()
    fun getMealPlanForDay(dayOfWeek: String): Flow<List<WeeklyMealPlanItem>> = dao.getMealPlanForDay(dayOfWeek)
    suspend fun setWeeklyMealPlan(items: List<WeeklyMealPlanItem>) {
        dao.clearWeeklyMealPlan()
        dao.insertWeeklyMealPlan(items)
    }
    suspend fun setMealPlanItemLogged(id: Long, isLogged: Boolean) = dao.setMealPlanItemLogged(id, isLogged)

    val chatMessages: Flow<List<ChatMessage>> = dao.getChatMessages()
    suspend fun addChatMessage(message: ChatMessage): Long = dao.insertChatMessage(message)
    suspend fun addChatMessages(messages: List<ChatMessage>) = dao.insertChatMessages(messages)
    suspend fun getChatMessageCount(): Int = dao.getChatMessageCount()
    suspend fun clearChat() = dao.clearChat()

    val weightLogs: Flow<List<WeightLog>> = dao.getWeightLogs()
    suspend fun logWeight(weightLog: WeightLog): Long = dao.insertWeightLog(weightLog)
    suspend fun addWeightLogs(weightLogs: List<WeightLog>) = dao.insertWeightLogs(weightLogs)
    suspend fun getWeightLogCount(): Int = dao.getWeightLogCount()
}
