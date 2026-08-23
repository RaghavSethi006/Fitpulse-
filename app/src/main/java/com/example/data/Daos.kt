package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FitPulseDao {

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    // Food Items
    @Query("SELECT * FROM food_items ORDER BY name ASC")
    fun getAllFoodItems(): Flow<List<FoodItem>>

    @Query("SELECT * FROM food_items WHERE name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchFoodItems(query: String): Flow<List<FoodItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(foodItem: FoodItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItems(foodItems: List<FoodItem>)

    @Query("DELETE FROM food_items WHERE id = :id")
    suspend fun deleteFoodItem(id: Long)

    // Meal Logs
    @Query("SELECT * FROM meal_logs WHERE date = :date ORDER BY loggedAt ASC")
    fun getMealLogsForDate(date: String): Flow<List<MealLog>>

    @Query("SELECT * FROM meal_logs ORDER BY loggedAt DESC LIMIT 100")
    fun getRecentMealLogs(): Flow<List<MealLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealLog(mealLog: MealLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealLogs(mealLogs: List<MealLog>)

    @Query("SELECT COUNT(*) FROM meal_logs")
    suspend fun getMealLogCount(): Int

    @Query("DELETE FROM meal_logs WHERE id = :id")
    suspend fun deleteMealLog(id: Long)

    // Exercises
    @Query("SELECT * FROM exercises ORDER BY category, name ASC")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE category = :category ORDER BY name ASC")
    fun getExercisesByCategory(category: String): Flow<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)

    @Query("SELECT * FROM exercises WHERE id = :id LIMIT 1")
    suspend fun getExerciseById(id: String): Exercise?

    // Workout Routines
    @Query("SELECT * FROM workout_routines ORDER BY id ASC")
    fun getAllRoutines(): Flow<List<WorkoutRoutine>>

    @Query("SELECT * FROM workout_routines WHERE id = :id LIMIT 1")
    suspend fun getRoutineById(id: Long): WorkoutRoutine?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: WorkoutRoutine): Long

    @Update
    suspend fun updateRoutine(routine: WorkoutRoutine)

    @Query("DELETE FROM workout_routines WHERE id = :id")
    suspend fun deleteRoutine(id: Long)

    // Workout History
    @Query("SELECT * FROM workout_history ORDER BY timestamp DESC")
    fun getAllWorkoutHistory(): Flow<List<WorkoutHistory>>

    @Query("SELECT * FROM workout_history WHERE date = :date")
    fun getWorkoutHistoryForDate(date: String): Flow<List<WorkoutHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutHistory(history: WorkoutHistory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutHistories(histories: List<WorkoutHistory>)

    @Query("SELECT COUNT(*) FROM workout_history")
    suspend fun getWorkoutHistoryCount(): Int

    // Weekly Meal Plan
    @Query("SELECT * FROM weekly_meal_plan ORDER BY id ASC")
    fun getWeeklyMealPlan(): Flow<List<WeeklyMealPlanItem>>

    @Query("SELECT * FROM weekly_meal_plan WHERE dayOfWeek = :dayOfWeek")
    fun getMealPlanForDay(dayOfWeek: String): Flow<List<WeeklyMealPlanItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyMealPlan(items: List<WeeklyMealPlanItem>)

    @Query("DELETE FROM weekly_meal_plan")
    suspend fun clearWeeklyMealPlan()

    @Query("UPDATE weekly_meal_plan SET isLogged = :isLogged WHERE id = :id")
    suspend fun setMealPlanItemLogged(id: Long, isLogged: Boolean)

    // Chat Messages
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessages(messages: List<ChatMessage>)

    @Query("SELECT COUNT(*) FROM chat_messages")
    suspend fun getChatMessageCount(): Int

    @Query("DELETE FROM chat_messages")
    suspend fun clearChat()

    // Weight Logs
    @Query("SELECT * FROM weight_logs ORDER BY timestamp ASC")
    fun getWeightLogs(): Flow<List<WeightLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightLog(weightLog: WeightLog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightLogs(weightLogs: List<WeightLog>)

    @Query("SELECT COUNT(*) FROM weight_logs")
    suspend fun getWeightLogCount(): Int
}
