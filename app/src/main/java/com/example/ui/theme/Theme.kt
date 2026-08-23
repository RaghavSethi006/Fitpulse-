package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BlueVibrant,
    onPrimary = Color.White,
    primaryContainer = BluePrimary,
    onPrimaryContainer = Color.White,
    secondary = IndigoAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E1B4B),
    onSecondaryContainer = IndigoLight,
    tertiary = VioletAccent,
    onTertiary = Color.White,
    background = ImmersiveBackground,
    onBackground = SlateTextPrimary,
    surface = ImmersiveCard,
    onSurface = SlateTextPrimary,
    surfaceVariant = ImmersiveCardDark,
    onSurfaceVariant = SlateTextSecondary,
    outline = ImmersiveBorder,
    outlineVariant = ImmersiveBorderSubtle
)

private val LightColorScheme = darkColorScheme(
    // Immersive UI is fundamentally a deep-space dark UI experience
    primary = BlueVibrant,
    onPrimary = Color.White,
    primaryContainer = BluePrimary,
    onPrimaryContainer = Color.White,
    secondary = IndigoAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E1B4B),
    onSecondaryContainer = IndigoLight,
    tertiary = VioletAccent,
    onTertiary = Color.White,
    background = ImmersiveBackground,
    onBackground = SlateTextPrimary,
    surface = ImmersiveCard,
    onSurface = SlateTextPrimary,
    surfaceVariant = ImmersiveCardDark,
    onSurfaceVariant = SlateTextSecondary,
    outline = ImmersiveBorder,
    outlineVariant = ImmersiveBorderSubtle
)

@Composable
fun FitPulseTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
