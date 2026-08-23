package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.MealLog
import com.example.data.PrepopulatedData
import com.example.ui.components.AiFoodScannerSheet
import com.example.ui.components.FoodSearchAndLogDialog
import com.example.ui.components.MacroSummaryCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(viewModel: FitnessViewModel) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val todayMealLogs by viewModel.todayMealLogs.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val foodDatabase by viewModel.foodItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val isScanningFood by viewModel.isScanningFood.collectAsState()
    val scannedFoodResult by viewModel.scannedFoodResult.collectAsState()
    val scanError by viewModel.scanError.collectAsState()

    var activeMealDialogType by remember { mutableStateOf<String?>(null) }
    var showAiScannerSheet by remember { mutableStateOf(false) }

    val user = userProfile ?: PrepopulatedData.defaultProfile

    val totalCalories = todayMealLogs.sumOf { it.calories }
    val totalProtein = todayMealLogs.sumOf { it.protein }
    val totalCarbs = todayMealLogs.sumOf { it.carbs }
    val totalFats = todayMealLogs.sumOf { it.fats }

    val mealTypes = listOf("BREAKFAST", "LUNCH", "DINNER", "SNACKS")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Date Selector & AI Scanner CTA
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ImmersiveCard,
                    border = BorderStroke(1.dp, ImmersiveBorderSubtle)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        IconButton(onClick = { viewModel.shiftDate(-1) }, modifier = Modifier.size(36.dp)) {
                            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Previous Day", tint = SlateTextSecondary)
                        }

                        Text(
                            text = selectedDate,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(onClick = { viewModel.shiftDate(1) }, modifier = Modifier.size(36.dp)) {
                            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next Day", tint = SlateTextSecondary)
                        }
                    }
                }

                Button(
                    onClick = { showAiScannerSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("ai_food_scanner_button")
                ) {
                    Icon(imageVector = Icons.Default.DocumentScanner, contentDescription = "Scan", tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("AI SCAN", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, letterSpacing = 0.5.sp)
                }
            }
        }

        // Macro Summary Progress Card
        item {
            MacroSummaryCard(
                currentCalories = totalCalories,
                targetCalories = user.dailyCalorieTarget,
                currentProtein = totalProtein,
                targetProtein = user.proteinTargetGrams,
                currentCarbs = totalCarbs,
                targetCarbs = user.carbsTargetGrams,
                currentFats = totalFats,
                targetFats = user.fatsTargetGrams
            )
        }

        // Meal Sections (Breakfast, Lunch, Dinner, Snacks)
        items(mealTypes) { mealType ->
            val logs = todayMealLogs.filter { it.mealType.equals(mealType, ignoreCase = true) }
            val mealCalories = logs.sumOf { it.calories }
            val mealProtein = logs.sumOf { it.protein }
            val mealCarbs = logs.sumOf { it.carbs }
            val mealFats = logs.sumOf { it.fats }

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
                                text = mealType.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )
                            if (logs.isNotEmpty()) {
                                Text(
                                    text = "${mealCalories.toInt()} kcal • P: ${mealProtein.toInt()}g • C: ${mealCarbs.toInt()}g • F: ${mealFats.toInt()}g",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SlateTextSecondary
                                )
                            }
                        }

                        IconButton(
                            onClick = { activeMealDialogType = mealType },
                            modifier = Modifier.testTag("add_meal_${mealType.lowercase()}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = "Add $mealType",
                                tint = BlueLight,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    if (logs.isEmpty()) {
                        Text(
                            text = "No items logged yet for $mealType",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextMuted,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(10.dp))
                        logs.forEach { logItem ->
                            MealLogRow(
                                log = logItem,
                                onDelete = { viewModel.deleteMealLog(logItem.id) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

    // Food Database & Recipe Search Modal Dialog
    activeMealDialogType?.let { mType ->
        FoodSearchAndLogDialog(
            mealType = mType,
            foodList = foodDatabase,
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.setSearchQuery(it) },
            onLogFood = { food, qty, unit ->
                viewModel.logMeal(mType, food, qty, unit)
            },
            onCreateCustomRecipe = { name, brand, category, servingSize, servingUnit, cal, p, c, f, ings ->
                viewModel.addCustomFoodItem(name, brand, category, servingSize, servingUnit, cal, p, c, f, 0.0, true, ings)
            },
            onDismiss = {
                activeMealDialogType = null
                viewModel.setSearchQuery("")
            }
        )
    }

    // AI Food Scanner Modal
    if (showAiScannerSheet) {
        AiFoodScannerSheet(
            isScanning = isScanningFood,
            scannedFood = scannedFoodResult,
            scanError = scanError,
            onScanRequest = { bitmap, text ->
                viewModel.scanFoodWithAi(bitmap, text)
            },
            onConfirmLogScannedFood = { food, targetMeal ->
                viewModel.logMeal(targetMeal, food, food.servingSize, food.servingUnit)
                viewModel.clearScannedResult()
                showAiScannerSheet = false
            },
            onDismiss = {
                showAiScannerSheet = false
                viewModel.clearScannedResult()
            }
        )
    }
}

@Composable
fun MealLogRow(
    log: MealLog,
    onDelete: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = ImmersiveCardInner,
        border = BorderStroke(1.dp, ImmersiveBorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = log.foodName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SlateTextPrimary
                )
                Text(
                    text = "${log.quantity.toInt()} ${log.servingUnit} • P: ${log.protein.toInt()}g, C: ${log.carbs.toInt()}g, F: ${log.fats.toInt()}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateTextSecondary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${log.calories.toInt()} kcal",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = EnergeticOrange
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete",
                        tint = SlateTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
