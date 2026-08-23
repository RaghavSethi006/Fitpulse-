package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        FoodItem::class,
        MealLog::class,
        Exercise::class,
        WorkoutRoutine::class,
        WorkoutHistory::class,
        WeeklyMealPlanItem::class,
        ChatMessage::class,
        WeightLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitPulseDatabase : RoomDatabase() {
    abstract fun dao(): FitPulseDao

    companion object {
        @Volatile
        private var INSTANCE: FitPulseDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FitPulseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitPulseDatabase::class.java,
                    "fitpulse_db"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.dao())
                    }
                }
            }

            suspend fun populateDatabase(dao: FitPulseDao) {
                // Insert default user profile
                dao.insertOrUpdateProfile(PrepopulatedData.defaultProfile)
                // Insert foods
                dao.insertFoodItems(PrepopulatedData.starterFoods)
                // Insert exercises
                dao.insertExercises(PrepopulatedData.starterExercises)
                // Insert starter routines
                val routines = PrepopulatedData.createInitialRoutines()
                routines.forEach { dao.insertRoutine(it) }
                // Insert starter weekly meal plan
                dao.insertWeeklyMealPlan(PrepopulatedData.starterWeeklyMealPlan)
                // Insert starter weight logs (30 days)
                dao.insertWeightLogs(PrepopulatedData.generatePastMonthWeightLogs())
                // Insert starter meal logs (30 days + today)
                dao.insertMealLogs(PrepopulatedData.generatePastMonthMealLogs())
                // Insert starter workout history (30 days)
                dao.insertWorkoutHistories(PrepopulatedData.generatePastMonthWorkoutHistory())
                // Insert starter chat messages
                dao.insertChatMessages(PrepopulatedData.starterChatMessages)
            }
        }
    }
}
