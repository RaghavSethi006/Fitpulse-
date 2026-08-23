package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Exercise
import com.example.data.RoutineExercise
import com.example.data.WorkoutRoutine
import com.example.tts.VoiceCoachManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.ActiveWorkoutState

@Composable
fun ActiveWorkoutSessionView(
    routine: WorkoutRoutine,
    exercisesList: List<RoutineExercise>,
    currentIndex: Int,
    currentSet: Int,
    workoutState: ActiveWorkoutState,
    timerSecondsRemaining: Int,
    totalElapsedSeconds: Int,
    exerciseLibrary: List<Exercise>,
    voiceCoach: VoiceCoachManager,
    onCompleteSet: () -> Unit,
    onSkipRest: () -> Unit,
    onFinishWorkout: () -> Unit,
    onCancelWorkout: () -> Unit
) {
    var isMuted by remember { mutableStateOf(voiceCoach.isMuted) }
    val currentExercise = exercisesList.getOrNull(currentIndex)
    val exerciseDetail = remember(currentExercise, exerciseLibrary) {
        exerciseLibrary.find { it.id == currentExercise?.exerciseId }
    }

    val elapsedMinutes = totalElapsedSeconds / 60
    val elapsedSecs = totalElapsedSeconds % 60
    val formattedElapsed = String.format("%02d:%02d", elapsedMinutes, elapsedSecs)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ImmersiveBackground
    ) {
        if (workoutState == ActiveWorkoutState.COMPLETED) {
            // Celebration Screen
            WorkoutCompletionScreen(
                routineName = routine.name,
                totalElapsed = formattedElapsed,
                exerciseCount = exercisesList.size,
                onDismiss = onCancelWorkout
            )
            return@Surface
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onCancelWorkout) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Exit Workout", tint = SlateTextPrimary)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = routine.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    Text(
                        text = "Time: $formattedElapsed",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = BlueLight
                    )
                }

                Row {
                    IconButton(
                        onClick = {
                            isMuted = !isMuted
                            voiceCoach.isMuted = isMuted
                        }
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = "Mute Voice Coach",
                            tint = if (isMuted) SlateTextMuted else BlueLight
                        )
                    }

                    IconButton(onClick = { voiceCoach.speakMotivation() }) {
                        Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = "Voice Coach Motivation", tint = EnergeticOrange)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Exercise Progress Indicator (e.g. Exercise 2 of 5)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Exercise ${currentIndex + 1} of ${exercisesList.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = SlateTextSecondary
                )
                Text(
                    text = "Set $currentSet of ${currentExercise?.targetSets ?: 3}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = BlueLight
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // REST MODE vs EXERCISING MODE
            if (workoutState == ActiveWorkoutState.RESTING) {
                // Rest Timer Display
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
                    border = BorderStroke(1.dp, ImmersiveBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "REST & RECOVER",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BlueLight,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Large Rest Countdown Circle
                        Box(
                            modifier = Modifier
                                .size(170.dp)
                                .clip(CircleShape)
                                .background(ImmersiveCardInner),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$timerSecondsRemaining",
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Black,
                                    color = SlateTextPrimary
                                )
                                Text(
                                    text = "seconds",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SlateTextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Up Next: ${currentExercise?.exerciseName} (Set ${if (currentSet < (currentExercise?.targetSets ?: 1)) currentSet + 1 else 1})",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SlateTextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        Button(
                            onClick = onSkipRest,
                            colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(50.dp)
                                .testTag("skip_rest_button")
                        ) {
                            Icon(imageVector = Icons.Default.FastForward, contentDescription = "Skip Rest", tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Skip Rest", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // Active Exercise Execution Card with 2D Animation Canvas
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentExercise?.exerciseName ?: "Exercise",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = SlateTextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2D Vector Animation Guide
                    ExerciseAnimationCanvas(
                        animationType = exerciseDetail?.animationType ?: "bench_press",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        accentColor = BlueLight
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Target Metric Row (Reps or Duration + Weight)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TargetInfoBadge(
                            title = "TARGET",
                            value = if ((currentExercise?.targetDurationSeconds ?: 0) > 0) {
                                "${currentExercise?.targetDurationSeconds}s"
                            } else {
                                "${currentExercise?.targetReps} Reps"
                            },
                            modifier = Modifier.weight(1f)
                        )

                        if ((currentExercise?.weightKg ?: 0.0) > 0) {
                            TargetInfoBadge(
                                title = "WEIGHT",
                                value = "${currentExercise?.weightKg} kg",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        TargetInfoBadge(
                            title = "REST",
                            value = "${currentExercise?.restTimeSeconds}s",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (!currentExercise?.notes.isNullOrBlank() || !exerciseDetail?.formTips.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = ImmersiveCard,
                            border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = "Tip", tint = BlueLight, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentExercise?.notes?.ifBlank { exerciseDetail?.formTips } ?: (exerciseDetail?.formTips ?: ""),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SlateTextSecondary
                                )
                            }
                        }
                    }

                    // Optional Countdown Timer for time-based exercises
                    if ((currentExercise?.targetDurationSeconds ?: 0) > 0 && timerSecondsRemaining > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Timer: $timerSecondsRemaining sec",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = EnergeticOrange
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            if (workoutState == ActiveWorkoutState.EXERCISING) {
                Button(
                    onClick = onCompleteSet,
                    colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("complete_set_button")
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Complete Set", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentSet == (currentExercise?.targetSets ?: 1) && currentIndex == exercisesList.size - 1) {
                            "Finish Final Set & Complete Workout"
                        } else {
                            "Complete Set $currentSet of ${currentExercise?.targetSets}"
                        },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onFinishWorkout) {
                Text("End Workout Session", color = Color(0xFFEF4444))
            }
        }
    }
}

@Composable
fun TargetInfoBadge(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ImmersiveCard,
        border = BorderStroke(1.dp, ImmersiveBorderSubtle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = SlateTextMuted, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
        }
    }
}

@Composable
fun WorkoutCompletionScreen(
    routineName: String,
    totalElapsed: String,
    exerciseCount: Int,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(BlueVibrant, IndigoAccent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Workout Complete",
                tint = Color.White,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "WORKOUT CRUSHED!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = SlateTextPrimary
        )

        Text(
            text = "Completed $routineName",
            style = MaterialTheme.typography.bodyLarge,
            color = SlateTextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
            border = BorderStroke(1.dp, ImmersiveBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Duration", style = MaterialTheme.typography.labelMedium, color = SlateTextSecondary)
                    Text(totalElapsed, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = BlueLight)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Exercises", style = MaterialTheme.typography.labelMedium, color = SlateTextSecondary)
                    Text("$exerciseCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = IndigoLight)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Volume", style = MaterialTheme.typography.labelMedium, color = SlateTextSecondary)
                    Text("Logged", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = EnergeticOrange)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("done_workout_button")
        ) {
            Text("Back to Dashboard", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
