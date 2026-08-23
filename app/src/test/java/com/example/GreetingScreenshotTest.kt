package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.ui.components.MacroSummaryCard
import com.example.ui.theme.FitPulseTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [34])
class GreetingScreenshotTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun macro_summary_screenshot() {
        composeTestRule.setContent {
            FitPulseTheme {
                MacroSummaryCard(
                    currentCalories = 1850.0,
                    targetCalories = 2600,
                    currentProtein = 145.0,
                    targetProtein = 180,
                    currentCarbs = 190.0,
                    targetCarbs = 280,
                    currentFats = 55.0,
                    targetFats = 70
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/macro_summary.png")
    }
}
