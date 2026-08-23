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
import com.example.data.WeeklyMealPlanItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiNutritionistScreen(viewModel: FitnessViewModel) {
    val weeklyPlan by viewModel.weeklyMealPlan.collectAsState()
    val isGenerating by viewModel.isGeneratingMealPlan.collectAsState()
    val nutritionistMessage by viewModel.nutritionistMessage.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var selectedDay by remember { mutableStateOf("Monday") }
    var showGenerateDialog by remember { mutableStateOf(false) }

    val dayMeals = remember(weeklyPlan, selectedDay) {
        weeklyPlan.filter { it.dayOfWeek.equals(selectedDay, ignoreCase = true) }
    }

    val dayTotalCalories = dayMeals.sumOf { it.calories }
    val dayTotalProtein = dayMeals.sumOf { it.protein }
    val dayTotalCarbs = dayMeals.sumOf { it.carbs }
    val dayTotalFats = dayMeals.sumOf { it.fats }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "AI NUTRITIONIST",
                    style = MaterialTheme.typography.labelSmall,
                    color = BlueLight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontSize = 10.sp
                )
                Text(
                    text = "Weekly Macro Strategy",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
            }

            Button(
                onClick = { showGenerateDialog = true },
                enabled = !isGenerating,
                colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("generate_ai_meal_plan_button")
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Generate", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Re-plan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Day of week selector chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(daysOfWeek) { day ->
                val isSelected = selectedDay == day
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = if (isSelected) BluePrimary.copy(alpha = 0.2f) else ImmersiveCard,
                    border = BorderStroke(1.dp, if (isSelected) BlueLight else ImmersiveBorderSubtle),
                    modifier = Modifier.clickable { selectedDay = day }
                ) {
                    Text(
                        text = day,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) BlueLight else SlateTextSecondary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Nutritionist message banner if present
        nutritionistMessage?.let { msg ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BluePrimary.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, BlueLight.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Status", tint = BlueLight, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = msg, style = MaterialTheme.typography.bodySmall, color = SlateTextPrimary)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Daily Macro Target Comparison Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
            border = BorderStroke(1.dp, ImmersiveBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$dayTotalCalories", fontWeight = FontWeight.Black, fontSize = 18.sp, color = EnergeticOrange)
                    Text(text = "Total kcal", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${dayTotalProtein.toInt()}g", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ProteinBlue)
                    Text(text = "Protein", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${dayTotalCarbs.toInt()}g", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CarbsAmber)
                    Text(text = "Carbs", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${dayTotalFats.toInt()}g", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FatRose)
                    Text(text = "Fats", style = MaterialTheme.typography.labelSmall, color = SlateTextSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Meal Cards
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            if (dayMeals.isEmpty()) {
                item {
                    Text(
                        text = "No meals generated for $selectedDay. Tap 'Re-plan' above to generate your customized week.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlateTextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(dayMeals) { mealItem ->
                    AiMealPlanCard(
                        item = mealItem,
                        onQuickLog = { viewModel.quickLogFromMealPlan(mealItem) }
                    )
                }
            }
        }
    }

    // Generate Meal Plan Dialog
    if (showGenerateDialog) {
        var customDietaryPreferences by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showGenerateDialog = false },
            title = {
                Text(text = "Generate 7-Day AI Meal Plan", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "The AI Nutritionist will calculate exact portions and recipes tailored to your goal (${userProfile?.goal}) and target (${userProfile?.dailyCalorieTarget} kcal).",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateTextSecondary
                    )

                    OutlinedTextField(
                        value = customDietaryPreferences,
                        onValueChange = { customDietaryPreferences = it },
                        label = { Text("Special Requests / Allergies / Dislikes") },
                        placeholder = { Text("e.g. Extra high protein breakfasts, no seafood, quick 15-min prep") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.generateWeeklyMealPlan(customDietaryPreferences)
                        showGenerateDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                    modifier = Modifier.testTag("confirm_generate_meal_plan_button")
                ) {
                    Text("Generate with AI", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showGenerateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AiMealPlanCard(
    item: WeeklyMealPlanItem,
    onQuickLog: () -> Unit
) {
    var loggedState by remember { mutableStateOf(item.isLogged) }

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
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = BluePrimary.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, BlueLight.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = item.mealType.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BlueLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "${item.calories} kcal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = EnergeticOrange
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary
            )

            if (item.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateTextSecondary
                )
            }

            if (item.ingredients.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Ingredients: ${item.ingredients}",
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateTextMuted
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("P: ${item.protein.toInt()}g", color = ProteinBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("C: ${item.carbs.toInt()}g", color = CarbsAmber, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("F: ${item.fats.toInt()}g", color = FatRose, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        onQuickLog()
                        loggedState = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (loggedState) ImmersiveCardInner else BlueVibrant
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(
                        imageVector = if (loggedState) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = "Log Meal",
                        tint = if (loggedState) BlueLight else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (loggedState) "Logged" else "Quick Log",
                        color = if (loggedState) BlueLight else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
