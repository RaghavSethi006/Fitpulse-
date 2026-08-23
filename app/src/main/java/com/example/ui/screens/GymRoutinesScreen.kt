package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Exercise
import com.example.data.WorkoutRoutine
import com.example.ui.components.ExerciseAnimationCanvas
import com.example.ui.components.RoutineBuilderDialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymRoutinesScreen(viewModel: FitnessViewModel) {
    val routines by viewModel.routines.collectAsState()
    val allExercises by viewModel.allExercises.collectAsState()
    val workoutHistory by viewModel.workoutHistory.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: My Routines, 1: Exercise Library & Animations, 2: History
    var showRoutineBuilder by remember { mutableStateOf(false) }
    var editingRoutine by remember { mutableStateOf<WorkoutRoutine?>(null) }
    var selectedExerciseDetail by remember { mutableStateOf<Exercise?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "WORKOUT HUB",
                    style = MaterialTheme.typography.labelSmall,
                    color = BlueLight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontSize = 10.sp
                )
                Text(
                    text = "Routines & Form",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
            }

            if (selectedTab == 0) {
                Button(
                    onClick = {
                        editingRoutine = null
                        showRoutineBuilder = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("create_routine_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Create Routine", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Segmented Tab Row
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ImmersiveCard,
            border = BorderStroke(1.dp, ImmersiveBorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = BlueLight,
                indicator = {},
                divider = {}
            ) {
                listOf("Routines (${routines.size})", "Exercise Guides", "History").forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) BluePrimary.copy(alpha = 0.2f) else Color.Transparent)
                            .padding(vertical = 10.dp),
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (isSelected) BlueLight else SlateTextSecondary
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
            0 -> {
                // Routines List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    if (routines.isEmpty()) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = ImmersiveCard,
                                border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = "Gym", tint = BlueLight, modifier = Modifier.size(40.dp))
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text("No custom gym routines yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                                    Text("Tap 'New' above to build a custom split!", style = MaterialTheme.typography.bodySmall, color = SlateTextSecondary)
                                }
                            }
                        }
                    } else {
                        items(routines) { routine ->
                            val exercises = viewModel.parseRoutineExercises(routine)
                            RoutineCard(
                                routine = routine,
                                exercisesCount = exercises.size,
                                onStartWorkout = { viewModel.startWorkout(routine) },
                                onEdit = {
                                    editingRoutine = routine
                                    showRoutineBuilder = true
                                },
                                onDelete = { viewModel.deleteRoutine(routine.id) }
                            )
                        }
                    }
                }
            }

            1 -> {
                // Exercise Guides & 2D Animation Explorer
                var selectedCategory by remember { mutableStateOf("All") }
                val categories = listOf("All", "Chest", "Back", "Legs", "Shoulders", "Arms", "Core", "Cardio")

                val filteredExercises = remember(allExercises, selectedCategory) {
                    if (selectedCategory == "All") allExercises else allExercises.filter { it.category.equals(selectedCategory, ignoreCase = true) }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categories) { cat ->
                                val isSelected = selectedCategory == cat
                                Surface(
                                    shape = RoundedCornerShape(100.dp),
                                    color = if (isSelected) BluePrimary.copy(alpha = 0.2f) else ImmersiveCard,
                                    border = BorderStroke(1.dp, if (isSelected) BlueLight else ImmersiveBorderSubtle),
                                    modifier = Modifier.clickable { selectedCategory = cat }
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) BlueLight else SlateTextSecondary,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }

                    items(filteredExercises) { ex ->
                        ExerciseGuideCard(
                            exercise = ex,
                            onClick = { selectedExerciseDetail = ex }
                        )
                    }
                }
            }

            2 -> {
                // Workout History Logs
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    if (workoutHistory.isEmpty()) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = ImmersiveCard,
                                border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp)
                            ) {
                                Text(
                                    text = "No recorded workout history. Complete a workout session to see your progress metrics here!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SlateTextSecondary,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                    } else {
                        items(workoutHistory) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(22.dp),
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
                                        Text(text = item.routineName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = SlateTextPrimary)
                                        Text(
                                            text = "${item.date} • ${item.durationSeconds / 60} mins • ${item.completedExercisesCount} exercises",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = SlateTextSecondary
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = "${item.caloriesBurned.toInt()} kcal", fontWeight = FontWeight.Bold, color = EnergeticOrange)
                                        if (item.totalVolumeKg > 0) {
                                            Text(text = "${item.totalVolumeKg.toInt()} kg", style = MaterialTheme.typography.labelSmall, color = BlueLight)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Routine Builder Dialog
    if (showRoutineBuilder) {
        RoutineBuilderDialog(
            initialRoutine = editingRoutine,
            exerciseLibrary = allExercises,
            onSaveRoutine = { id, name, desc, day, dur, exs ->
                viewModel.saveRoutine(id, name, desc, day, dur, exs)
            },
            onDismiss = {
                showRoutineBuilder = false
                editingRoutine = null
            }
        )
    }

    // Detailed Exercise 2D Animation Viewer Dialog
    selectedExerciseDetail?.let { ex ->
        AlertDialog(
            onDismissRequest = { selectedExerciseDetail = null },
            title = {
                Text(text = ex.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ExerciseAnimationCanvas(
                        animationType = ex.animationType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        accentColor = BlueLight
                    )

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ImmersiveCardInner,
                        border = BorderStroke(1.dp, ImmersiveBorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = "Target Muscles: ${ex.primaryMuscle} (${ex.secondaryMuscles})", style = MaterialTheme.typography.labelMedium, color = BlueLight, fontWeight = FontWeight.Bold)
                            Text(text = "Equipment: ${ex.equipment}", style = MaterialTheme.typography.bodySmall, color = SlateTextSecondary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Form Cue: ${ex.formTips}", style = MaterialTheme.typography.bodySmall, color = SlateTextPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedExerciseDetail = null },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant)
                ) {
                    Text("Close", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun RoutineCard(
    routine: WorkoutRoutine,
    exercisesCount: Int,
    onStartWorkout: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BluePrimary.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, BlueLight.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = routine.dayOfWeek,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BlueLight,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "~${routine.targetDurationMinutes} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateTextSecondary
                    )
                }

                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = SlateTextSecondary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = routine.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )

            if (routine.description.isNotBlank()) {
                Text(
                    text = routine.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$exercisesCount exercises included",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = IndigoLight
                )

                Button(
                    onClick = onStartWorkout,
                    colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("START", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ExerciseGuideCard(
    exercise: Exercise,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
        border = BorderStroke(1.dp, ImmersiveBorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IndigoAccent.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = exercise.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = IndigoLight,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Target: ${exercise.primaryMuscle} • ${exercise.equipment}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateTextSecondary
                )
                Text(
                    text = exercise.formTips,
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateTextMuted,
                    maxLines = 1
                )
            }

            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = "View 2D Animation",
                tint = BlueLight,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
