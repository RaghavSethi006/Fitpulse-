package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.PrepopulatedData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name string resource`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FitPulse", appName)
    }

    @Test
    fun `verify starter prepopulated foods and exercises exist`() {
        assertTrue(PrepopulatedData.starterFoods.isNotEmpty())
        assertTrue(PrepopulatedData.starterExercises.isNotEmpty())
        assertEquals("FitPulse Athlete", PrepopulatedData.defaultProfile.name)
    }
}
