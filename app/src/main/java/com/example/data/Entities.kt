package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Alex",
    val age: Int = 26,
    val gender: String = "Male",
    val heightCm: Double = 178.0,
    val weightKg: Double = 75.0,
    val targetWeightKg: Double = 72.0,
    val activityLevel: String = "Moderately Active", // Sedentary, Lightly Active, Moderately Active, Very Active
    val goal: String = "Muscle Building", // Muscle Building, Fat Loss, Maintenance, Strength & Power, Endurance
    val experienceLevel: String = "Intermediate", // Beginner, Intermediate, Advanced
    val workoutSplit: String = "Push / Pull / Legs", // Full Body, Upper / Lower, Push / Pull / Legs, Bro Split, Custom
    val equipment: String = "Full Gym", // Full Gym, Home Dumbbells, Bodyweight, Resistance Bands
    val dietaryPreference: String = "High Protein", // Balanced, High Protein, Keto, Vegetarian, Vegan, Mediterranean
    val medicalNotes: String = "None. Focus on good posture and knee safety.",
    val dailyCalorieTarget: Int = 2400,
    val proteinTargetGrams: Int = 160,
    val carbsTargetGrams: Int = 260,
    val fatsTargetGrams: Int = 70,
    val waterTargetMl: Int = 3000
)

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val brand: String = "Whole Foods",
    val category: String, // Protein, Carbs, Dairy & Eggs, Fruits & Veggies, Fats, Snacks, Beverages, Custom Recipe
    val servingSize: Double = 100.0,
    val servingUnit: String = "g", // g, oz, piece, cup, scoop, tbsp, ml
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fats: Double,
    val fiber: Double = 0.0,
    val isCustomRecipe: Boolean = false,
    val recipeIngredients: String = ""
)

@Entity(tableName = "meal_logs")
data class MealLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val mealType: String, // BREAKFAST, LUNCH, DINNER, SNACKS
    val foodItemId: Long? = null,
    val foodName: String,
    val quantity: Double,
    val servingUnit: String,
    val calories: Double,
    val protein: Double,
    val carbs: Double,
    val fats: Double,
    val loggedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // Chest, Back, Legs, Shoulders, Arms, Core, Cardio
    val equipment: String, // Barbell, Dumbbell, Cable, Machine, Bodyweight, Cardio Machine
    val primaryMuscle: String,
    val secondaryMuscles: String,
    val instructions: String,
    val formTips: String,
    val animationType: String, // bench_press, squat, deadlift, bicep_curl, overhead_press, lat_pulldown, pushup, running, plank, lateral_raise, lunge, tricep_dips
    val isTimeBased: Boolean = false
)

@Entity(tableName = "workout_routines")
data class WorkoutRoutine(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val dayOfWeek: String = "Monday", // Monday, Tuesday, etc. or Any Day
    val targetDurationMinutes: Int = 45,
    val exercisesJson: String // JSON array of RoutineExercise
)

@JsonClass(generateAdapter = true)
data class RoutineExercise(
    val exerciseId: String,
    val exerciseName: String,
    val targetSets: Int = 3,
    val targetReps: Int = 10,
    val targetDurationSeconds: Int = 0,
    val weightKg: Double = 0.0,
    val restTimeSeconds: Int = 60,
    val notes: String = ""
)

@Entity(tableName = "workout_history")
data class WorkoutHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val routineId: Long? = null,
    val routineName: String,
    val durationSeconds: Int,
    val totalVolumeKg: Double,
    val caloriesBurned: Double,
    val completedExercisesCount: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "weekly_meal_plan")
data class WeeklyMealPlanItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayOfWeek: String, // Monday .. Sunday
    val mealType: String, // Breakfast, Lunch, Dinner, Snack
    val title: String,
    val description: String,
    val ingredients: String,
    val calories: Int,
    val protein: Double,
    val carbs: Double,
    val fats: Double,
    val isLogged: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val role: String, // "user", "coach", "nutritionist"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "weight_logs")
data class WeightLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val weightKg: Double,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
