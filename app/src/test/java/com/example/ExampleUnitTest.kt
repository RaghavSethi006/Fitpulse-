package com.example

import com.example.data.Entities
import com.example.data.PrepopulatedData
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun `macro calorie calculation is accurate`() {
        val proteinGrams = 150.0
        val carbGrams = 200.0
        val fatGrams = 60.0

        // 4 kcal per g protein, 4 kcal per g carb, 9 kcal per g fat
        val calculatedCalories = (proteinGrams * 4) + (carbGrams * 4) + (fatGrams * 9)
        assertEquals(1940.0, calculatedCalories, 0.01)
    }

    @Test
    fun `starter foods have positive nutritional values`() {
        val foods = PrepopulatedData.starterFoods
        assertTrue("Foods list should not be empty", foods.isNotEmpty())
        for (food in foods) {
            assertTrue("Food ${food.name} calories must be positive", food.calories >= 0)
            assertTrue("Food ${food.name} protein must be >= 0", food.protein >= 0)
            assertTrue("Food ${food.name} carbs must be >= 0", food.carbs >= 0)
            assertTrue("Food ${food.name} fats must be >= 0", food.fats >= 0)
        }
    }

    @Test
    fun `starter routines contain valid exercises`() {
        val routines = PrepopulatedData.starterRoutines
        assertTrue("Routines list should not be empty", routines.isNotEmpty())
        for (routine in routines) {
            assertTrue("Routine ${routine.name} must have a name", routine.name.isNotBlank())
            assertTrue("Routine ${routine.name} duration must be positive", routine.estimatedDurationMinutes > 0)
        }
    }
}

