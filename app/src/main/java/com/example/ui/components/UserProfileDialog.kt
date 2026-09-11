package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.UserProfile
import com.example.ui.theme.*
import com.example.ui.viewmodel.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileDialog(
    currentProfile: UserProfile,
    viewModel: FitnessViewModel? = null,
    onSaveProfile: (UserProfile) -> Unit,
    onDismiss: () -> Unit
) {
    var showApiKeyDialog by remember { mutableStateOf(false) }
    val hasActiveKey = viewModel?.hasActiveGeminiKey?.collectAsState()?.value ?: false

    if (showApiKeyDialog && viewModel != null) {
        GeminiApiKeyDialog(
            viewModel = viewModel,
            onDismiss = { showApiKeyDialog = false }
        )
    }

    var name by remember { mutableStateOf(currentProfile.name) }
    var ageText by remember { mutableStateOf(currentProfile.age.toString()) }
    var gender by remember { mutableStateOf(currentProfile.gender) }
    var weightText by remember { mutableStateOf(currentProfile.weightKg.toString()) }
    var targetWeightText by remember { mutableStateOf(currentProfile.targetWeightKg.toString()) }
    var heightText by remember { mutableStateOf(currentProfile.heightCm.toString()) }

    var goal by remember { mutableStateOf(currentProfile.goal) }
    var experienceLevel by remember { mutableStateOf(currentProfile.experienceLevel) }
    var workoutSplit by remember { mutableStateOf(currentProfile.workoutSplit) }
    var equipment by remember { mutableStateOf(currentProfile.equipment) }
    var dietaryPref by remember { mutableStateOf(currentProfile.dietaryPreference) }
    var medicalNotes by remember { mutableStateOf(currentProfile.medicalNotes) }

    var caloriesText by remember { mutableStateOf(currentProfile.dailyCalorieTarget.toString()) }
    var proteinText by remember { mutableStateOf(currentProfile.proteinTargetGrams.toString()) }
    var carbsText by remember { mutableStateOf(currentProfile.carbsTargetGrams.toString()) }
    var fatsText by remember { mutableStateOf(currentProfile.fatsTargetGrams.toString()) }

    val goals = listOf("Muscle Building (Hypertrophy)", "Fat Loss & Cutting", "Strength & Power", "Athletic Endurance", "Body Recomposition")
    val splits = listOf("Push / Pull / Legs", "Upper / Lower Split", "Full Body Circuit", "Body Part Split (Bro Split)", "Cardio & Calisthenics")
    val diets = listOf("High Protein Balanced", "Low Carb / Keto", "Vegetarian", "Vegan", "Mediterranean")

    fun autoCalculateMacros() {
        val w = weightText.toDoubleOrNull() ?: 75.0
        val h = heightText.toDoubleOrNull() ?: 178.0
        val a = ageText.toIntOrNull() ?: 26

        // Mifflin-St Jeor Equation
        val bmr = (10 * w) + (6.25 * h) - (5 * a) + 5
        val tdee = bmr * 1.55 // moderate activity

        val targetCal = when (goal) {
            "Muscle Building (Hypertrophy)" -> (tdee + 350).toInt()
            "Fat Loss & Cutting" -> (tdee - 450).toInt()
            "Strength & Power" -> (tdee + 200).toInt()
            "Athletic Endurance" -> (tdee + 150).toInt()
            else -> tdee.toInt()
        }

        val pGrams = (w * 2.2).toInt()
        val fGrams = (w * 0.9).toInt()
        val remainingCalForCarbs = (targetCal - (pGrams * 4) - (fGrams * 9)).coerceAtLeast(400)
        val cGrams = (remainingCalForCarbs / 4)

        caloriesText = targetCal.toString()
        proteinText = pGrams.toString()
        carbsText = cGrams.toString()
        fatsText = fGrams.toString()
    }

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
                            text = "Athlete Profile & Goals",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Calibrates AI Nutritionist, Coach & Tracker",
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
                            label = { Text("Name", color = SlateTextSecondary) },
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
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = ageText,
                                onValueChange = { ageText = it },
                                label = { Text("Age", color = SlateTextSecondary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
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
                            OutlinedTextField(
                                value = heightText,
                                onValueChange = { heightText = it },
                                label = { Text("Height (cm)", color = SlateTextSecondary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
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
                    }

                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = weightText,
                                onValueChange = { weightText = it },
                                label = { Text("Current Wt (kg)", color = SlateTextSecondary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
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
                            OutlinedTextField(
                                value = targetWeightText,
                                onValueChange = { targetWeightText = it },
                                label = { Text("Target Wt (kg)", color = SlateTextSecondary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f),
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
                    }

                    // Goal Selector
                    item {
                        Text("Fitness Goal:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        goals.forEach { g ->
                            val isSelected = goal == g
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) BluePrimary.copy(alpha = 0.2f) else ImmersiveCardInner,
                                border = BorderStroke(1.dp, if (isSelected) BlueLight else ImmersiveBorderSubtle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable { goal = g }
                            ) {
                                Text(
                                    text = g,
                                    color = if (isSelected) BlueLight else SlateTextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }

                    // Split Selector
                    item {
                        Text("Preferred Split:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        splits.forEach { s ->
                            val isSelected = workoutSplit == s
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) IndigoAccent.copy(alpha = 0.2f) else ImmersiveCardInner,
                                border = BorderStroke(1.dp, if (isSelected) IndigoLight else ImmersiveBorderSubtle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable { workoutSplit = s }
                            ) {
                                Text(
                                    text = s,
                                    color = if (isSelected) IndigoLight else SlateTextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }

                    // Dietary Preference
                    item {
                        Text("Dietary Preference:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        diets.forEach { d ->
                            val isSelected = dietaryPref == d
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) BluePrimary.copy(alpha = 0.2f) else ImmersiveCardInner,
                                border = BorderStroke(1.dp, if (isSelected) BlueLight else ImmersiveBorderSubtle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable { dietaryPref = d }
                            ) {
                                Text(
                                    text = d,
                                    color = if (isSelected) BlueLight else SlateTextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = medicalNotes,
                            onValueChange = { medicalNotes = it },
                            label = { Text("Health / Injuries / Dietary Notes", color = SlateTextSecondary) },
                            placeholder = { Text("e.g. Mild lower back sensitivity, lactose intolerant", color = SlateTextMuted) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
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

                    // Gemini AI API Key Settings
                    if (viewModel != null) {
                        item {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("AI Intelligence:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = ImmersiveCardInner,
                                border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showApiKeyDialog = true }
                                    .testTag("profile_gemini_api_key_tile")
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VpnKey,
                                            contentDescription = null,
                                            tint = BlueLight,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "Google Gemini API Key",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                color = SlateTextPrimary
                                            )
                                            Text(
                                                text = if (hasActiveKey) "Configured & Active" else "Tap to configure your personal key",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (hasActiveKey) GreenAccent else EnergeticOrange
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = SlateTextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Auto-calculate macros button
                    item {
                        Button(
                            onClick = { autoCalculateMacros() },
                            colors = ButtonDefaults.buttonColors(containerColor = ImmersiveCardInner),
                            border = BorderStroke(1.dp, BlueLight.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Calculate, contentDescription = "Calculate", tint = BlueLight)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Auto-Calculate Daily Targets", color = BlueLight, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Daily Target Inputs
                    item {
                        Text("Daily Targets:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = caloriesText,
                                onValueChange = { caloriesText = it },
                                label = { Text("Calories", color = SlateTextSecondary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = ImmersiveCardInner,
                                    unfocusedContainerColor = ImmersiveCardInner,
                                    focusedBorderColor = BlueLight,
                                    unfocusedBorderColor = ImmersiveBorderSubtle,
                                    focusedTextColor = SlateTextPrimary,
                                    unfocusedTextColor = SlateTextPrimary
                                )
                            )
                            OutlinedTextField(
                                value = proteinText,
                                onValueChange = { proteinText = it },
                                label = { Text("Protein (g)", color = SlateTextSecondary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
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
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = carbsText,
                                onValueChange = { carbsText = it },
                                label = { Text("Carbs (g)", color = SlateTextSecondary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = ImmersiveCardInner,
                                    unfocusedContainerColor = ImmersiveCardInner,
                                    focusedBorderColor = BlueLight,
                                    unfocusedBorderColor = ImmersiveBorderSubtle,
                                    focusedTextColor = SlateTextPrimary,
                                    unfocusedTextColor = SlateTextPrimary
                                )
                            )
                            OutlinedTextField(
                                value = fatsText,
                                onValueChange = { fatsText = it },
                                label = { Text("Fats (g)", color = SlateTextSecondary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
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
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val updated = currentProfile.copy(
                            name = name.ifBlank { "Athlete" },
                            age = ageText.toIntOrNull() ?: currentProfile.age,
                            gender = gender,
                            weightKg = weightText.toDoubleOrNull() ?: currentProfile.weightKg,
                            targetWeightKg = targetWeightText.toDoubleOrNull() ?: currentProfile.targetWeightKg,
                            heightCm = heightText.toDoubleOrNull() ?: currentProfile.heightCm,
                            goal = goal,
                            experienceLevel = experienceLevel,
                            workoutSplit = workoutSplit,
                            equipment = equipment,
                            dietaryPreference = dietaryPref,
                            medicalNotes = medicalNotes,
                            dailyCalorieTarget = caloriesText.toIntOrNull() ?: currentProfile.dailyCalorieTarget,
                            proteinTargetGrams = proteinText.toIntOrNull() ?: currentProfile.proteinTargetGrams,
                            carbsTargetGrams = carbsText.toIntOrNull() ?: currentProfile.carbsTargetGrams,
                            fatsTargetGrams = fatsText.toIntOrNull() ?: currentProfile.fatsTargetGrams
                        )
                        onSaveProfile(updated)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("save_profile_button")
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Save", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Athlete Profile", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
