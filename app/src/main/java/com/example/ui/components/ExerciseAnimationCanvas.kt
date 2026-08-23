package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EnergeticOrange
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ExerciseAnimationCanvas(
    animationType: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    accentColor: Color = EmeraldPrimary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "exercise_animation")

    // Phase cycle 0.0f -> 1.0f (representing one full rep)
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rep_phase"
    )

    // Continuous running phase
    val continuousPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "running_phase"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(DarkSurfaceVariant, Color(0xFF090E17))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val w = size.width
            val h = size.height
            val cx = w / 2f
            val cy = h / 2f

            // Grid background accents
            val gridColor = Color(0x15FFFFFF)
            for (i in 0..4) {
                val y = h * (i + 1) / 6f
                drawLine(gridColor, Offset(0f, y), Offset(w, y), strokeWidth = 1.dp.toPx())
            }

            val bodyColor = Color(0xFFF1F5F9)
            val jointColor = accentColor
            val equipmentColor = ElectricCyan
            val strokeWidth = 6.dp.toPx()
            val headRadius = 12.dp.toPx()
            val jointRadius = 5.dp.toPx()

            when (animationType.lowercase()) {
                "bench_press" -> {
                    // Bench base
                    drawLine(Color(0xFF475569), Offset(cx - 90.dp.toPx(), cy + 20.dp.toPx()), Offset(cx + 90.dp.toPx(), cy + 20.dp.toPx()), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(Color(0xFF334155), Offset(cx - 70.dp.toPx(), cy + 20.dp.toPx()), Offset(cx - 70.dp.toPx(), cy + 60.dp.toPx()), strokeWidth = 6.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(Color(0xFF334155), Offset(cx + 70.dp.toPx(), cy + 20.dp.toPx()), Offset(cx + 70.dp.toPx(), cy + 60.dp.toPx()), strokeWidth = 6.dp.toPx(), cap = StrokeCap.Round)

                    // Torso lying flat
                    val headX = cx - 55.dp.toPx()
                    val headY = cy + 10.dp.toPx()
                    val shoulderX = cx - 35.dp.toPx()
                    val shoulderY = cy + 12.dp.toPx()
                    val hipX = cx + 25.dp.toPx()
                    val hipY = cy + 12.dp.toPx()
                    val kneeX = cx + 55.dp.toPx()
                    val kneeY = cy + 35.dp.toPx()
                    val footX = cx + 55.dp.toPx()
                    val footY = cy + 60.dp.toPx()

                    // Head & Body
                    drawCircle(bodyColor, headRadius, Offset(headX, headY))
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(hipX, hipY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(hipX, hipY), Offset(kneeX, kneeY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(kneeX, kneeY), Offset(footX, footY), strokeWidth = strokeWidth, cap = StrokeCap.Round)

                    // Arm and Barbell motion
                    val barTravel = 42.dp.toPx() * (1f - phase)
                    val barY = cy - 25.dp.toPx() + barTravel
                    val elbowX = shoulderX - 10.dp.toPx() * (1f - phase)
                    val elbowY = cy + 8.dp.toPx() + 15.dp.toPx() * (1f - phase)
                    val handX = shoulderX + 5.dp.toPx()
                    val handY = barY

                    // Arm
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(elbowX, elbowY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(elbowX, elbowY), Offset(handX, handY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawCircle(jointColor, jointRadius, Offset(elbowX, elbowY))

                    // Barbell & Weights
                    drawLine(equipmentColor, Offset(handX - 55.dp.toPx(), barY), Offset(handX + 55.dp.toPx(), barY), strokeWidth = 5.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(EnergeticOrange, 10.dp.toPx(), Offset(handX - 50.dp.toPx(), barY))
                    drawCircle(EnergeticOrange, 10.dp.toPx(), Offset(handX + 50.dp.toPx(), barY))
                }

                "squat" -> {
                    val squatDepth = 38.dp.toPx() * phase
                    val hipShiftX = 18.dp.toPx() * phase

                    val floorY = cy + 65.dp.toPx()
                    val ankleX = cx - 10.dp.toPx()
                    val ankleY = floorY - 5.dp.toPx()
                    val kneeX = cx + 8.dp.toPx() + (12.dp.toPx() * phase)
                    val kneeY = cy + 25.dp.toPx() + (squatDepth * 0.5f)
                    val hipX = cx - 8.dp.toPx() - hipShiftX
                    val hipY = cy - 10.dp.toPx() + squatDepth
                    val shoulderX = cx - 2.dp.toPx() - (hipShiftX * 0.4f)
                    val shoulderY = cy - 45.dp.toPx() + squatDepth
                    val headX = cx - 2.dp.toPx()
                    val headY = shoulderY - 18.dp.toPx()

                    // Legs
                    drawLine(bodyColor, Offset(ankleX, ankleY), Offset(kneeX, kneeY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(kneeX, kneeY), Offset(hipX, hipY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(hipX, hipY), Offset(shoulderX, shoulderY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawCircle(bodyColor, headRadius, Offset(headX, headY))

                    // Barbell on shoulders
                    drawLine(equipmentColor, Offset(shoulderX - 45.dp.toPx(), shoulderY), Offset(shoulderX + 45.dp.toPx(), shoulderY), strokeWidth = 5.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(jointColor, 10.dp.toPx(), Offset(shoulderX - 40.dp.toPx(), shoulderY))
                    drawCircle(jointColor, 10.dp.toPx(), Offset(shoulderX + 40.dp.toPx(), shoulderY))

                    // Arm gripping bar
                    val elbowX = shoulderX - 10.dp.toPx()
                    val elbowY = shoulderY + 15.dp.toPx()
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(elbowX, elbowY), strokeWidth = strokeWidth * 0.8f, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(elbowX, elbowY), Offset(shoulderX + 8.dp.toPx(), shoulderY), strokeWidth = strokeWidth * 0.8f, cap = StrokeCap.Round)

                    drawCircle(jointColor, jointRadius, Offset(kneeX, kneeY))
                    drawCircle(jointColor, jointRadius, Offset(hipX, hipY))
                }

                "deadlift" -> {
                    val hinge = phase // 0 standing, 1 bent over
                    val hipX = cx - (30.dp.toPx() * hinge)
                    val hipY = cy - 10.dp.toPx() + (22.dp.toPx() * hinge)
                    val kneeX = cx - (5.dp.toPx() * hinge)
                    val kneeY = cy + 28.dp.toPx()
                    val footX = cx
                    val footY = cy + 65.dp.toPx()

                    val shoulderX = cx + (15.dp.toPx() * hinge)
                    val shoulderY = cy - 48.dp.toPx() + (45.dp.toPx() * hinge)
                    val headX = shoulderX + (10.dp.toPx() * hinge)
                    val headY = shoulderY - 18.dp.toPx()

                    val barX = footX + 5.dp.toPx()
                    val barY = cy - 15.dp.toPx() + (70.dp.toPx() * hinge)

                    // Draw body
                    drawLine(bodyColor, Offset(footX, footY), Offset(kneeX, kneeY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(kneeX, kneeY), Offset(hipX, hipY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(hipX, hipY), Offset(shoulderX, shoulderY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawCircle(bodyColor, headRadius, Offset(headX, headY))

                    // Arms hanging straight to bar
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(barX, barY), strokeWidth = strokeWidth, cap = StrokeCap.Round)

                    // Barbell on floor
                    drawLine(equipmentColor, Offset(barX - 45.dp.toPx(), barY), Offset(barX + 45.dp.toPx(), barY), strokeWidth = 5.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(EnergeticOrange, 12.dp.toPx(), Offset(barX - 40.dp.toPx(), barY))
                    drawCircle(EnergeticOrange, 12.dp.toPx(), Offset(barX + 40.dp.toPx(), barY))

                    drawCircle(jointColor, jointRadius, Offset(hipX, hipY))
                    drawCircle(jointColor, jointRadius, Offset(kneeX, kneeY))
                }

                "bicep_curl" -> {
                    // Standing straight
                    val headX = cx - 10.dp.toPx()
                    val headY = cy - 50.dp.toPx()
                    val shoulderX = cx - 10.dp.toPx()
                    val shoulderY = cy - 30.dp.toPx()
                    val hipX = cx - 10.dp.toPx()
                    val hipY = cy + 10.dp.toPx()
                    val feetX = cx - 10.dp.toPx()
                    val feetY = cy + 65.dp.toPx()

                    drawCircle(bodyColor, headRadius, Offset(headX, headY))
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(hipX, hipY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(hipX, hipY), Offset(feetX, feetY), strokeWidth = strokeWidth, cap = StrokeCap.Round)

                    // Upper arm pinned
                    val elbowX = shoulderX + 2.dp.toPx()
                    val elbowY = shoulderY + 28.dp.toPx()
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(elbowX, elbowY), strokeWidth = strokeWidth, cap = StrokeCap.Round)

                    // Forearm flexing up (phase 0 down, phase 1 up)
                    val curlAngle = -Math.PI / 2 + (phase * Math.PI * 0.75)
                    val forearmLen = 25.dp.toPx()
                    val handX = elbowX + (forearmLen * sin(curlAngle)).toFloat()
                    val handY = elbowY + (forearmLen * cos(curlAngle)).toFloat()

                    drawLine(bodyColor, Offset(elbowX, elbowY), Offset(handX, handY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawCircle(jointColor, jointRadius, Offset(elbowX, elbowY))

                    // Dumbbell
                    drawLine(equipmentColor, Offset(handX - 8.dp.toPx(), handY), Offset(handX + 8.dp.toPx(), handY), strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(accentColor, 7.dp.toPx(), Offset(handX - 8.dp.toPx(), handY))
                    drawCircle(accentColor, 7.dp.toPx(), Offset(handX + 8.dp.toPx(), handY))
                }

                "overhead_press" -> {
                    val headX = cx
                    val headY = cy - 45.dp.toPx()
                    val shoulderX = cx
                    val shoulderY = cy - 25.dp.toPx()
                    val hipX = cx
                    val hipY = cy + 15.dp.toPx()
                    val footX = cx
                    val footY = cy + 65.dp.toPx()

                    drawCircle(bodyColor, headRadius, Offset(headX, headY))
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(hipX, hipY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(hipX, hipY), Offset(footX, footY), strokeWidth = strokeWidth, cap = StrokeCap.Round)

                    // Press motion from shoulder to lockout
                    val barY = cy - 25.dp.toPx() - (45.dp.toPx() * phase)
                    val elbowY = cy - 5.dp.toPx() - (20.dp.toPx() * phase)
                    val elbowX = shoulderX - 16.dp.toPx() + (10.dp.toPx() * phase)

                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(elbowX, elbowY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(elbowX, elbowY), Offset(cx - 20.dp.toPx(), barY), strokeWidth = strokeWidth, cap = StrokeCap.Round)

                    // Barbell overhead
                    drawLine(equipmentColor, Offset(cx - 48.dp.toPx(), barY), Offset(cx + 48.dp.toPx(), barY), strokeWidth = 5.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(EnergeticOrange, 9.dp.toPx(), Offset(cx - 44.dp.toPx(), barY))
                    drawCircle(EnergeticOrange, 9.dp.toPx(), Offset(cx + 44.dp.toPx(), barY))
                }

                "pushup" -> {
                    val pushDepth = 25.dp.toPx() * phase
                    val footX = cx + 60.dp.toPx()
                    val footY = cy + 30.dp.toPx()

                    val hipX = cx + 15.dp.toPx()
                    val hipY = cy + 18.dp.toPx() + (pushDepth * 0.7f)
                    val shoulderX = cx - 35.dp.toPx()
                    val shoulderY = cy + 10.dp.toPx() + pushDepth
                    val headX = cx - 52.dp.toPx()
                    val headY = shoulderY - 4.dp.toPx()

                    val handX = shoulderX + 5.dp.toPx()
                    val handY = cy + 30.dp.toPx()
                    val elbowX = shoulderX - 10.dp.toPx() + (10.dp.toPx() * phase)
                    val elbowY = (shoulderY + handY) / 2f - (12.dp.toPx() * (1f - phase))

                    // Floor line
                    drawLine(Color(0xFF475569), Offset(cx - 80.dp.toPx(), cy + 32.dp.toPx()), Offset(cx + 80.dp.toPx(), cy + 32.dp.toPx()), strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)

                    // Body
                    drawLine(bodyColor, Offset(footX, footY), Offset(hipX, hipY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(hipX, hipY), Offset(shoulderX, shoulderY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawCircle(bodyColor, headRadius, Offset(headX, headY))

                    // Arm
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(elbowX, elbowY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(elbowX, elbowY), Offset(handX, handY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawCircle(jointColor, jointRadius, Offset(elbowX, elbowY))
                }

                "running" -> {
                    val runPhase = continuousPhase
                    val bounce = 4.dp.toPx() * kotlin.math.abs(sin(runPhase))
                    val headX = cx
                    val headY = cy - 45.dp.toPx() + bounce
                    val shoulderX = cx - 2.dp.toPx()
                    val shoulderY = cy - 25.dp.toPx() + bounce
                    val hipX = cx - 4.dp.toPx()
                    val hipY = cy + 10.dp.toPx() + bounce

                    drawCircle(bodyColor, headRadius, Offset(headX, headY))
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(hipX, hipY), strokeWidth = strokeWidth, cap = StrokeCap.Round)

                    // Leg 1 (Front/Back)
                    val leg1Angle = sin(runPhase) * 0.8
                    val knee1X = hipX + (20.dp.toPx() * sin(leg1Angle)).toFloat()
                    val knee1Y = hipY + (22.dp.toPx() * cos(leg1Angle)).toFloat()
                    val foot1X = knee1X + (20.dp.toPx() * sin(leg1Angle + 0.4)).toFloat()
                    val foot1Y = knee1Y + (20.dp.toPx() * cos(leg1Angle + 0.4)).toFloat()

                    // Leg 2 (Opposite)
                    val leg2Angle = sin(runPhase + Math.PI) * 0.8
                    val knee2X = hipX + (20.dp.toPx() * sin(leg2Angle)).toFloat()
                    val knee2Y = hipY + (22.dp.toPx() * cos(leg2Angle)).toFloat()
                    val foot2X = knee2X + (20.dp.toPx() * sin(leg2Angle + 0.4)).toFloat()
                    val foot2Y = knee2Y + (20.dp.toPx() * cos(leg2Angle + 0.4)).toFloat()

                    // Draw back leg
                    drawLine(Color(0xFF94A3B8), Offset(hipX, hipY), Offset(knee2X, knee2Y), strokeWidth = strokeWidth * 0.8f, cap = StrokeCap.Round)
                    drawLine(Color(0xFF94A3B8), Offset(knee2X, knee2Y), Offset(foot2X, foot2Y), strokeWidth = strokeWidth * 0.8f, cap = StrokeCap.Round)

                    // Draw front leg
                    drawLine(bodyColor, Offset(hipX, hipY), Offset(knee1X, knee1Y), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(knee1X, knee1Y), Offset(foot1X, foot1Y), strokeWidth = strokeWidth, cap = StrokeCap.Round)

                    // Arms pumping
                    val arm1Angle = sin(runPhase + Math.PI) * 0.7
                    val hand1X = shoulderX + (25.dp.toPx() * sin(arm1Angle)).toFloat()
                    val hand1Y = shoulderY + (22.dp.toPx() * cos(arm1Angle)).toFloat()
                    drawLine(accentColor, Offset(shoulderX, shoulderY), Offset(hand1X, hand1Y), strokeWidth = strokeWidth * 0.8f, cap = StrokeCap.Round)

                    // Treadmill track line
                    drawLine(Color(0xFF334155), Offset(cx - 70.dp.toPx(), cy + 62.dp.toPx()), Offset(cx + 70.dp.toPx(), cy + 62.dp.toPx()), strokeWidth = 5.dp.toPx(), cap = StrokeCap.Round)
                }

                "plank" -> {
                    val footX = cx + 60.dp.toPx()
                    val footY = cy + 25.dp.toPx()
                    val hipX = cx + 15.dp.toPx()
                    val hipY = cy + 22.dp.toPx()
                    val shoulderX = cx - 35.dp.toPx()
                    val shoulderY = cy + 18.dp.toPx()
                    val headX = cx - 52.dp.toPx()
                    val headY = shoulderY - 2.dp.toPx()

                    val elbowX = shoulderX
                    val elbowY = cy + 32.dp.toPx()
                    val handX = elbowX - 15.dp.toPx()
                    val handY = elbowY

                    // Floor mat
                    drawLine(ElectricCyan, Offset(cx - 75.dp.toPx(), cy + 34.dp.toPx()), Offset(cx + 75.dp.toPx(), cy + 34.dp.toPx()), strokeWidth = 5.dp.toPx(), cap = StrokeCap.Round)

                    // Glowing core pulse
                    val glowAlpha = 0.3f + 0.3f * phase
                    drawCircle(EmeraldPrimary.copy(alpha = glowAlpha), 25.dp.toPx(), Offset(hipX - 10.dp.toPx(), hipY))

                    // Rigid body
                    drawLine(bodyColor, Offset(footX, footY), Offset(hipX, hipY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(hipX, hipY), Offset(shoulderX, shoulderY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawCircle(bodyColor, headRadius, Offset(headX, headY))

                    // Forearm plank
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(elbowX, elbowY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(elbowX, elbowY), Offset(handX, handY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawCircle(jointColor, jointRadius, Offset(elbowX, elbowY))
                }

                else -> {
                    // Default compound lift figure with animated rep glow
                    val headX = cx
                    val headY = cy - 40.dp.toPx() + (10.dp.toPx() * phase)
                    val shoulderX = cx
                    val shoulderY = cy - 20.dp.toPx() + (10.dp.toPx() * phase)
                    val hipX = cx
                    val hipY = cy + 15.dp.toPx() + (15.dp.toPx() * phase)
                    val footX = cx
                    val footY = cy + 60.dp.toPx()

                    drawCircle(bodyColor, headRadius, Offset(headX, headY))
                    drawLine(bodyColor, Offset(shoulderX, shoulderY), Offset(hipX, hipY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(bodyColor, Offset(hipX, hipY), Offset(footX, footY), strokeWidth = strokeWidth, cap = StrokeCap.Round)

                    // Dumbbells in hands
                    val handLX = shoulderX - 25.dp.toPx()
                    val handRX = shoulderX + 25.dp.toPx()
                    val handY = shoulderY + (15.dp.toPx() * (1f - phase))
                    drawLine(accentColor, Offset(shoulderX, shoulderY), Offset(handLX, handY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawLine(accentColor, Offset(shoulderX, shoulderY), Offset(handRX, handY), strokeWidth = strokeWidth, cap = StrokeCap.Round)
                    drawCircle(jointColor, 7.dp.toPx(), Offset(handLX, handY))
                    drawCircle(jointColor, 7.dp.toPx(), Offset(handRX, handY))
                }
            }
        }
    }
}
