package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.Exercise
import com.example.data.RoutineExercise
import com.example.data.WorkoutRoutine
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineBuilderDialog(
    initialRoutine: WorkoutRoutine? = null,
    exerciseLibrary: List<Exercise>,
    onSaveRoutine: (Long, String, String, String, Int, List<RoutineExercise>) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialRoutine?.name ?: "") }
    var description by remember { mutableStateOf(initialRoutine?.description ?: "") }
    var selectedDay by remember { mutableStateOf(initialRoutine?.dayOfWeek ?: "Monday") }
    var targetDurationMinutesText by remember { mutableStateOf((initialRoutine?.targetDurationMinutes ?: 45).toString()) }

    var routineExercises by remember { mutableStateOf<List<RoutineExercise>>(emptyList()) }
    var showExercisePicker by remember { mutableStateOf(false) }

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Flexible")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = ImmersiveCard,
            border = BorderStroke(1.dp, ImmersiveBorder),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (initialRoutine == null) "Create Gym Routine" else "Edit Routine",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Customize split, exercises, sets, reps & timers",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SlateTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Routine Name (e.g. Push Day Heavy)", color = SlateTextSecondary) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("routine_name_input"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ImmersiveCardInner,
                                unfocusedContainerColor = ImmersiveCardInner,
                                focusedBorderColor = BlueLight,
                                unfocusedBorderColor = ImmersiveBorderSubtle,
                                focusedTextColor = SlateTextPrimary,
                                unfocusedTextColor = SlateTextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Focus / Description (e.g. Chest & Triceps power)", color = SlateTextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ImmersiveCardInner,
                                unfocusedContainerColor = ImmersiveCardInner,
                                focusedBorderColor = BlueLight,
                                unfocusedBorderColor = ImmersiveBorderSubtle,
                                focusedTextColor = SlateTextPrimary,
                                unfocusedTextColor = SlateTextPrimary
                            )
                        )
                    }

                    item {
                        Text(
                            text = "Assigned Day of Week:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(daysOfWeek) { day ->
                                val isSelected = selectedDay == day
                                Surface(
                                    shape = RoundedCornerShape(100.dp),
                                    color = if (isSelected) BluePrimary.copy(alpha = 0.2f) else ImmersiveCardInner,
                                    border = BorderStroke(1.dp, if (isSelected) BlueLight else ImmersiveBorderSubtle),
                                    modifier = Modifier.clickable { selectedDay = day }
                                ) {
                                    Text(
                                        text = day,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) BlueLight else SlateTextSecondary,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = targetDurationMinutesText,
                            onValueChange = { targetDurationMinutesText = it },
                            label = { Text("Target Duration (Minutes)", color = SlateTextSecondary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ImmersiveCardInner,
                                unfocusedContainerColor = ImmersiveCardInner,
                                focusedBorderColor = BlueLight,
                                unfocusedBorderColor = ImmersiveBorderSubtle,
                                focusedTextColor = SlateTextPrimary,
                                unfocusedTextColor = SlateTextPrimary
                            )
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Exercises (${routineExercises.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )

                            Button(
                                onClick = { showExercisePicker = true },
                                colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("add_exercise_to_routine_button")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Exercise", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (routineExercises.isEmpty()) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = ImmersiveCardInner,
                                border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = "Gym",
                                        tint = SlateTextMuted,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "No exercises added yet",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = SlateTextPrimary
                                    )
                                    Text(
                                        text = "Tap 'Add Exercise' to select from library",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SlateTextSecondary
                                    )
                                }
                            }
                        }
                    } else {
                        items(routineExercises.size) { index ->
                            val ex = routineExercises[index]
                            RoutineExerciseConfigCard(
                                item = ex,
                                onUpdate = { updated ->
                                    val list = routineExercises.toMutableList()
                                    list[index] = updated
                                    routineExercises = list
                                },
                                onDelete = {
                                    val list = routineExercises.toMutableList()
                                    list.removeAt(index)
                                    routineExercises = list
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val duration = targetDurationMinutesText.toIntOrNull() ?: 45
                        onSaveRoutine(
                            initialRoutine?.id ?: 0L,
                            name,
                            description,
                            selectedDay,
                            duration,
                            routineExercises
                        )
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("save_routine_button"),
                    enabled = name.isNotBlank() && routineExercises.isNotEmpty()
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Save", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Routine (${routineExercises.size} exercises)", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Exercise Library Picker Dialog
    if (showExercisePicker) {
        ExercisePickerModal(
            exercises = exerciseLibrary,
            onSelectExercise = { selectedEx ->
                val newRoutineEx = RoutineExercise(
                    exerciseId = selectedEx.id,
                    exerciseName = selectedEx.name,
                    targetSets = 3,
                    targetReps = if (selectedEx.isTimeBased) 0 else 10,
                    targetDurationSeconds = if (selectedEx.isTimeBased) 60 else 0,
                    weightKg = 0.0,
                    restTimeSeconds = 60,
                    notes = ""
                )
                routineExercises = routineExercises + newRoutineEx
                showExercisePicker = false
            },
            onDismiss = { showExercisePicker = false }
        )
    }
}

@Composable
fun RoutineExerciseConfigCard(
    item: RoutineExercise,
    onUpdate: (RoutineExercise) -> Unit,
    onDelete: () -> Unit
) {
    var setsText by remember { mutableStateOf(item.targetSets.toString()) }
    var repsText by remember { mutableStateOf(item.targetReps.toString()) }
    var durationText by remember { mutableStateOf(item.targetDurationSeconds.toString()) }
    var weightText by remember { mutableStateOf(if (item.weightKg > 0) item.weightKg.toString() else "") }
    var restText by remember { mutableStateOf(item.restTimeSeconds.toString()) }
    var notesText by remember { mutableStateOf(item.notes) }

    val isTimeMode = item.targetDurationSeconds > 0 || item.targetReps == 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveCardInner),
        border = BorderStroke(1.dp, ImmersiveBorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.exerciseName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sets & (Reps or Time)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = setsText,
                    onValueChange = {
                        setsText = it
                        onUpdate(item.copy(targetSets = it.toIntOrNull() ?: 1))
                    },
                    label = { Text("Sets", color = SlateTextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ImmersiveCard,
                        unfocusedContainerColor = ImmersiveCard,
                        focusedBorderColor = BlueLight,
                        unfocusedBorderColor = ImmersiveBorderSubtle,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )

                if (isTimeMode) {
                    OutlinedTextField(
                        value = durationText,
                        onValueChange = {
                            durationText = it
                            onUpdate(item.copy(targetDurationSeconds = it.toIntOrNull() ?: 30, targetReps = 0))
                        },
                        label = { Text("Time (s)", color = SlateTextSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ImmersiveCard,
                            unfocusedContainerColor = ImmersiveCard,
                            focusedBorderColor = BlueLight,
                            unfocusedBorderColor = ImmersiveBorderSubtle,
                            focusedTextColor = SlateTextPrimary,
                            unfocusedTextColor = SlateTextPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    OutlinedTextField(
                        value = repsText,
                        onValueChange = {
                            repsText = it
                            onUpdate(item.copy(targetReps = it.toIntOrNull() ?: 10, targetDurationSeconds = 0))
                        },
                        label = { Text("Reps", color = SlateTextSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ImmersiveCard,
                            unfocusedContainerColor = ImmersiveCard,
                            focusedBorderColor = BlueLight,
                            unfocusedBorderColor = ImmersiveBorderSubtle,
                            focusedTextColor = SlateTextPrimary,
                            unfocusedTextColor = SlateTextPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = weightText,
                    onValueChange = {
                        weightText = it
                        onUpdate(item.copy(weightKg = it.toDoubleOrNull() ?: 0.0))
                    },
                    label = { Text("Wt (kg)", color = SlateTextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ImmersiveCard,
                        unfocusedContainerColor = ImmersiveCard,
                        focusedBorderColor = BlueLight,
                        unfocusedBorderColor = ImmersiveBorderSubtle,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = restText,
                    onValueChange = {
                        restText = it
                        onUpdate(item.copy(restTimeSeconds = it.toIntOrNull() ?: 60))
                    },
                    label = { Text("Rest (s)", color = SlateTextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ImmersiveCard,
                        unfocusedContainerColor = ImmersiveCard,
                        focusedBorderColor = BlueLight,
                        unfocusedBorderColor = ImmersiveBorderSubtle,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = notesText,
                    onValueChange = {
                        notesText = it
                        onUpdate(item.copy(notes = it))
                    },
                    label = { Text("Notes / Form cue", color = SlateTextSecondary) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ImmersiveCard,
                        unfocusedContainerColor = ImmersiveCard,
                        focusedBorderColor = BlueLight,
                        unfocusedBorderColor = ImmersiveBorderSubtle,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    ),
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}

@Composable
fun ExercisePickerModal(
    exercises: List<Exercise>,
    onSelectExercise: (Exercise) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Chest", "Back", "Legs", "Shoulders", "Arms", "Core", "Cardio")

    val filtered = remember(exercises, selectedCategory) {
        if (selectedCategory == "All") exercises else exercises.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = ImmersiveCard,
            border = BorderStroke(1.dp, ImmersiveBorder),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Exercise",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SlateTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = if (isSelected) BluePrimary.copy(alpha = 0.2f) else ImmersiveCardInner,
                            border = BorderStroke(1.dp, if (isSelected) BlueLight else ImmersiveBorderSubtle),
                            modifier = Modifier.clickable { selectedCategory = cat }
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) BlueLight else SlateTextSecondary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered) { ex ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectExercise(ex) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = ImmersiveCardInner),
                            border = BorderStroke(1.dp, ImmersiveBorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ex.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = SlateTextPrimary
                                    )
                                    Text(
                                        text = "${ex.category} • ${ex.equipment}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SlateTextSecondary
                                    )
                                    Text(
                                        text = "Primary: ${ex.primaryMuscle}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = BlueLight
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Add",
                                    tint = BlueLight
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
