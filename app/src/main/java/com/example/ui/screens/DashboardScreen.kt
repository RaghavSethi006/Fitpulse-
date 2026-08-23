package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.MacroSummaryCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: FitnessViewModel,
    onNavigateToNutrition: () -> Unit,
    onNavigateToGym: () -> Unit,
    onNavigateToAiNutritionist: () -> Unit,
    onNavigateToAiCoach: () -> Unit,
    onOpenProfile: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    val todayLogs by viewModel.todayMealLogs.collectAsState()
    val routines by viewModel.routines.collectAsState()
    val workoutHistory by viewModel.workoutHistory.collectAsState()
    val weeklyMealPlan by viewModel.weeklyMealPlan.collectAsState()
    val weightLogs by viewModel.weightLogs.collectAsState()

    var showWeightLogDialog by remember { mutableStateOf(false) }

    val totalCalories = todayLogs.sumOf { it.calories }
    val totalProtein = todayLogs.sumOf { it.protein }
    val totalCarbs = todayLogs.sumOf { it.carbs }
    val totalFats = todayLogs.sumOf { it.fats }

    val user = profile ?: PrepopulatedData.defaultProfile
    val todayRoutine = routines.firstOrNull()
    val nextExerciseName = if (todayRoutine != null) {
        val list = viewModel.parseRoutineExercises(todayRoutine)
        list.firstOrNull()?.exerciseName ?: "Strength Training"
    } else {
        "Dumbbell Bench Press"
    }

    val userInitials = remember(user.name) {
        val parts = user.name.trim().split(" ")
        if (parts.size >= 2) "${parts[0].take(1)}${parts[1].take(1)}".uppercase()
        else user.name.take(2).uppercase()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Immersive Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PERSONAL ECOSYSTEM",
                        style = MaterialTheme.typography.labelSmall,
                        color = BlueLight,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "FitPulse Alpha",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary,
                        letterSpacing = (-0.5).sp
                    )
                }

                // Glowing Gradient Avatar Frame
                Box(
                    modifier = Modifier
                        .clickable(onClick = onOpenProfile)
                        .testTag("profile_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.sweepGradient(
                                    listOf(BlueVibrant, IndigoAccent, VioletAccent, BlueVibrant)
                                )
                            )
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(ImmersiveCardInner),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userInitials,
                                color = SlateTextPrimary,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                    // Green online indicator
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(GreenAccent)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }

        // Daily Fuel & Macro Card
        item {
            MacroSummaryCard(
                currentCalories = totalCalories,
                targetCalories = user.dailyCalorieTarget,
                currentProtein = totalProtein,
                targetProtein = user.proteinTargetGrams,
                currentCarbs = totalCarbs,
                targetCarbs = user.carbsTargetGrams,
                currentFats = totalFats,
                targetFats = user.fatsTargetGrams,
                modifier = Modifier.clickable(onClick = onNavigateToNutrition)
            )
        }

        // Current Routine Card (Immersive UI Style)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
                border = BorderStroke(1.dp, ImmersiveBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(ImmersiveCard, ImmersiveCardDark)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(EnergeticOrange.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = "Workout",
                                    tint = EnergeticOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Current Routine",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )
                        }

                        Text(
                            text = todayRoutine?.dayOfWeek ?: "Day 1: Upper Body",
                            style = MaterialTheme.typography.labelMedium,
                            color = SlateTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Nested Exercise Focus Box
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = ImmersiveCardInner,
                        border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(SlateDarkPill),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = "Exercise",
                                        tint = BlueLight,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "NEXT EXERCISE",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = BlueLight,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.2.sp,
                                        fontSize = 10.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = nextExerciseName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SlateTextPrimary
                                    )
                                    Text(
                                        text = "${todayRoutine?.name ?: "Strength Split"} • ~${todayRoutine?.targetDurationMinutes ?: 45} mins",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SlateTextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    if (todayRoutine != null) {
                                        viewModel.startWorkout(todayRoutine)
                                    } else {
                                        onNavigateToGym()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("start_today_workout_button")
                            ) {
                                Text(
                                    text = "START WORKOUT SESSION",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.8.sp,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // AI Mentor Insight Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateToAiCoach),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
                border = BorderStroke(1.dp, ImmersiveBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(IndigoAccent, PurpleAccent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "AI Insight",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AI Mentor Insight",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = IndigoLight
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "\"Target at least ${user.proteinTargetGrams}g of protein today to optimize recovery for your '${user.goal}' protocol.\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 18.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open Coach",
                        tint = SlateTextMuted
                    )
                }
            }
        }

        // Quick Ecosystem Navigation Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ImmersiveQuickActionCard(
                    title = "AI Coach",
                    subtitle = "Live Guidance",
                    icon = Icons.Default.SmartToy,
                    accentColor = IndigoLight,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToAiCoach
                )

                ImmersiveQuickActionCard(
                    title = "AI Diet",
                    subtitle = "7-Day Plan",
                    icon = Icons.Default.AutoAwesome,
                    accentColor = BlueLight,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToAiNutritionist
                )

                ImmersiveQuickActionCard(
                    title = "Gym Hub",
                    subtitle = "${routines.size} Routines",
                    icon = Icons.Default.FitnessCenter,
                    accentColor = EnergeticOrange,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToGym
                )
            }
        }

        // Body Weight Progress
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
                border = BorderStroke(1.dp, ImmersiveBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Body Weight Progress",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )
                            Text(
                                text = "Current: ${user.weightKg} kg • Target: ${user.targetWeightKg} kg",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextSecondary
                            )
                        }

                        IconButton(
                            onClick = { showWeightLogDialog = true },
                            modifier = Modifier.testTag("log_weight_button")
                        ) {
                            Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Log Weight", tint = BlueLight)
                        }
                    }

                    if (weightLogs.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            weightLogs.takeLast(4).reversed().forEach { log ->
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = ImmersiveCardInner,
                                    border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "${log.weightKg}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BlueLight)
                                        Text(text = log.date.takeLast(5), fontSize = 10.sp, color = SlateTextMuted)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Recent Workout Consistency & History
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Activity Logs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )

                Text(
                    text = "${workoutHistory.size} sessions",
                    style = MaterialTheme.typography.labelSmall,
                    color = BlueLight,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (workoutHistory.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ImmersiveCard,
                    border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No recorded sessions yet. Complete your first workout to view tracked volume and metrics!",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateTextSecondary,
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
        } else {
            items(workoutHistory.take(3)) { history ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
                    border = BorderStroke(1.dp, ImmersiveBorderSubtle)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = history.routineName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, color = SlateTextPrimary)
                            Text(
                                text = "${history.date} • ${history.durationSeconds / 60} mins • ${history.completedExercisesCount} exercises",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextSecondary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${history.caloriesBurned.toInt()} kcal",
                                fontWeight = FontWeight.Bold,
                                color = EnergeticOrange,
                                style = MaterialTheme.typography.titleSmall
                            )
                            if (history.totalVolumeKg > 0) {
                                Text(
                                    text = "${history.totalVolumeKg.toInt()} kg lifted",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BlueLight
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Weight Log Dialog
    if (showWeightLogDialog) {
        var weightInput by remember { mutableStateOf(user.weightKg.toString()) }
        var notesInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showWeightLogDialog = false },
            title = { Text("Log Body Weight", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Notes (e.g. morning fasting)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val wt = weightInput.toDoubleOrNull() ?: user.weightKg
                        viewModel.logWeight(wt, notesInput)
                        showWeightLogDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant)
                ) {
                    Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWeightLogDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ImmersiveQuickActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = ImmersiveCard,
        border = BorderStroke(1.dp, ImmersiveBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary, fontSize = 10.sp)
        }
    }
}
