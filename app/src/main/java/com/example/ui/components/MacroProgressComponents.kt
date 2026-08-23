package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MacroSummaryCard(
    currentCalories: Double,
    targetCalories: Int,
    currentProtein: Double,
    targetProtein: Int,
    currentCarbs: Double,
    targetCarbs: Int,
    currentFats: Double,
    targetFats: Int,
    modifier: Modifier = Modifier
) {
    val calPercentage = if (targetCalories > 0) ((currentCalories / targetCalories) * 100).toInt().coerceIn(0, 999) else 0

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = ImmersiveCard
        ),
        border = BorderStroke(1.dp, ImmersiveBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            BluePrimary.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        radius = 400f
                    )
                )
                .padding(22.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header with Fuel Title & Percentage Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Daily Fuel",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = SlateTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "%,d".format(currentCalories.toInt()),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = SlateTextPrimary,
                                fontSize = 32.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "/ %,d kcal".format(targetCalories),
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlateTextMuted
                            )
                        }
                    }

                    // Status Pill Badge
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = BluePrimary.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, BlueLight.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "$calPercentage% DAILY GOAL",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp,
                            color = BlueLight,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 3 Macro Mini Cards in Row (Immersive Grid)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ImmersiveMacroMiniCard(
                        name = "PROTEIN",
                        current = currentProtein,
                        target = targetProtein,
                        accentColor = ProteinBlue,
                        modifier = Modifier.weight(1f)
                    )
                    ImmersiveMacroMiniCard(
                        name = "CARBS",
                        current = currentCarbs,
                        target = targetCarbs,
                        accentColor = CarbsAmber,
                        modifier = Modifier.weight(1f)
                    )
                    ImmersiveMacroMiniCard(
                        name = "FATS",
                        current = currentFats,
                        target = targetFats,
                        accentColor = FatRose,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ImmersiveMacroMiniCard(
    name: String,
    current: Double,
    target: Int,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val progress = if (target > 0) (current / target).toFloat().coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(700), label = name)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = ImmersiveCardInner,
        border = BorderStroke(1.dp, ImmersiveBorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = SlateTextMuted,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${current.toInt()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "g",
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateTextMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Thin Pill Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(SlateDarkPill)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(accentColor)
                )
            }
        }
    }
}
