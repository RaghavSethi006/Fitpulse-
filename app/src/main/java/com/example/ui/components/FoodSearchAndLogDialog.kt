package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
import com.example.data.FoodItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchAndLogDialog(
    mealType: String,
    foodList: List<FoodItem>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onLogFood: (FoodItem, Double, String) -> Unit,
    onCreateCustomRecipe: (String, String, String, Double, String, Double, Double, Double, Double, String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Database Search, 1: Create Custom Food/Recipe
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf("All") }

    val categories = listOf("All", "Protein", "Carbs & Grains", "Dairy & Eggs", "Fruits & Veggies", "Fats", "Snacks", "Meals & Recipes")

    val filteredFoods = remember(foodList, selectedCategoryFilter) {
        if (selectedCategoryFilter == "All") {
            foodList
        } else {
            foodList.filter { it.category.contains(selectedCategoryFilter, ignoreCase = true) }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = ImmersiveCard,
            border = BorderStroke(1.dp, ImmersiveBorder),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
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
                            text = "Add to $mealType",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Search database or create custom meal",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SlateTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs: Search DB vs Custom Recipe
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ImmersiveCardInner,
                    border = BorderStroke(1.dp, ImmersiveBorderSubtle)
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = BlueLight,
                        indicator = {},
                        divider = {}
                    ) {
                        listOf("Food Database", "Custom Recipe").forEachIndexed { index, title ->
                            val isSelected = selectedTab == index
                            Tab(
                                selected = isSelected,
                                onClick = { selectedTab = index },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) BluePrimary.copy(alpha = 0.25f) else Color.Transparent)
                                    .padding(vertical = 10.dp),
                                text = {
                                    Text(
                                        text = title,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) BlueLight else SlateTextSecondary,
                                        fontSize = 13.sp
                                    )
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedTab == 0) {
                    // Database search view
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search 100+ foods, brands, ingredients...", color = SlateTextMuted) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = SlateTextSecondary) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = SlateTextSecondary)
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ImmersiveCardInner,
                            unfocusedContainerColor = ImmersiveCardInner,
                            focusedBorderColor = BlueLight,
                            unfocusedBorderColor = ImmersiveBorderSubtle,
                            focusedTextColor = SlateTextPrimary,
                            unfocusedTextColor = SlateTextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("food_search_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category filter chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { cat ->
                            val isSelected = selectedCategoryFilter == cat
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = if (isSelected) BluePrimary.copy(alpha = 0.2f) else ImmersiveCardInner,
                                border = BorderStroke(1.dp, if (isSelected) BlueLight else ImmersiveBorderSubtle),
                                modifier = Modifier.clickable { selectedCategoryFilter = cat }
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) BlueLight else SlateTextSecondary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Food list
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredFoods) { food ->
                            FoodItemRow(
                                food = food,
                                isSelected = selectedFood?.id == food.id,
                                onClick = { selectedFood = food }
                            )
                        }
                    }
                } else {
                    // Custom Recipe Builder Tab
                    CustomRecipeBuilderView(
                        onSaveRecipe = { name, brand, category, servingSize, servingUnit, cal, p, c, f, ings ->
                            onCreateCustomRecipe(name, brand, category, servingSize, servingUnit, cal, p, c, f, ings)
                            selectedTab = 0
                        }
                    )
                }

                // Food Portion Quantity & Log Confirmation Modal
                selectedFood?.let { food ->
                    PortionLogSheet(
                        food = food,
                        onConfirm = { qty, unit ->
                            onLogFood(food, qty, unit)
                            selectedFood = null
                            onDismiss()
                        },
                        onDismiss = { selectedFood = null }
                    )
                }
            }
        }
    }
}

@Composable
fun FoodItemRow(
    food: FoodItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("food_row_${food.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) BluePrimary.copy(alpha = 0.15f) else ImmersiveCardInner),
        border = BorderStroke(1.dp, if (isSelected) BlueLight else ImmersiveBorderSubtle)
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
                        text = food.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    if (food.isCustomRecipe) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = IndigoAccent.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Recipe",
                                style = MaterialTheme.typography.labelSmall,
                                color = IndigoLight,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${food.brand} • Per ${food.servingSize.toInt()} ${food.servingUnit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateTextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Macro pill badges
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "P: ${food.protein.toInt()}g",
                        style = MaterialTheme.typography.labelSmall,
                        color = ProteinBlue,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "C: ${food.carbs.toInt()}g",
                        style = MaterialTheme.typography.labelSmall,
                        color = CarbsAmber,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "F: ${food.fats.toInt()}g",
                        style = MaterialTheme.typography.labelSmall,
                        color = FatRose,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${food.calories.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = EnergeticOrange
                )
                Text(
                    text = "kcal",
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Select Portion",
                    tint = BlueLight,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PortionLogSheet(
    food: FoodItem,
    onConfirm: (Double, String) -> Unit,
    onDismiss: () -> Unit
) {
    var quantityText by remember { mutableStateOf(food.servingSize.toString()) }
    var selectedUnit by remember { mutableStateOf(food.servingUnit) }

    val qty = quantityText.toDoubleOrNull() ?: food.servingSize
    val ratio = if (food.servingSize > 0) qty / food.servingSize else 1.0

    val calcCal = (food.calories * ratio).coerceAtLeast(0.0)
    val calcP = (food.protein * ratio).coerceAtLeast(0.0)
    val calcC = (food.carbs * ratio).coerceAtLeast(0.0)
    val calcF = (food.fats * ratio).coerceAtLeast(0.0)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Adjust Portion: ${food.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("portion_quantity_input")
                    )

                    OutlinedTextField(
                        value = selectedUnit,
                        onValueChange = { selectedUnit = it },
                        label = { Text("Unit") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Computed summary
                Card(
                    colors = CardDefaults.cardColors(containerColor = ImmersiveCardInner),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, ImmersiveBorderSubtle)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "${calcCal.toInt()} Calories",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EnergeticOrange
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Protein: ${calcP.toInt()}g", color = ProteinBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Carbs: ${calcC.toInt()}g", color = CarbsAmber, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Fats: ${calcF.toInt()}g", color = FatRose, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(qty, selectedUnit) },
                colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                modifier = Modifier.testTag("confirm_log_food_button")
            ) {
                Text("Log to Meal", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CustomRecipeBuilderView(
    onSaveRecipe: (String, String, String, Double, String, Double, Double, Double, Double, String) -> Unit
) {
    var recipeName by remember { mutableStateOf("") }
    var recipeIngredients by remember { mutableStateOf("") }
    var servingCountText by remember { mutableStateOf("1") }
    var caloriesText by remember { mutableStateOf("") }
    var proteinText by remember { mutableStateOf("") }
    var carbsText by remember { mutableStateOf("") }
    var fatsText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            OutlinedTextField(
                value = recipeName,
                onValueChange = { recipeName = it },
                label = { Text("Recipe / Dish Name", color = SlateTextSecondary) },
                placeholder = { Text("e.g. Protein Banana Pancakes", color = SlateTextMuted) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ImmersiveCardInner,
                    unfocusedContainerColor = ImmersiveCardInner,
                    focusedBorderColor = BlueLight,
                    unfocusedBorderColor = ImmersiveBorderSubtle,
                    focusedTextColor = SlateTextPrimary,
                    unfocusedTextColor = SlateTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("recipe_name_input")
            )
        }

        item {
            OutlinedTextField(
                value = recipeIngredients,
                onValueChange = { recipeIngredients = it },
                label = { Text("Ingredients List", color = SlateTextSecondary) },
                placeholder = { Text("e.g. 50g Oats, 1 Scoop Whey, 1 Banana", color = SlateTextMuted) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ImmersiveCardInner,
                    unfocusedContainerColor = ImmersiveCardInner,
                    focusedBorderColor = BlueLight,
                    unfocusedBorderColor = ImmersiveBorderSubtle,
                    focusedTextColor = SlateTextPrimary,
                    unfocusedTextColor = SlateTextPrimary
                ),
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = caloriesText,
                    onValueChange = { caloriesText = it },
                    label = { Text("Calories (kcal)", color = SlateTextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ImmersiveCardInner,
                        unfocusedContainerColor = ImmersiveCardInner,
                        focusedBorderColor = BlueLight,
                        unfocusedBorderColor = ImmersiveBorderSubtle,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = servingCountText,
                    onValueChange = { servingCountText = it },
                    label = { Text("Servings", color = SlateTextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ImmersiveCardInner,
                        unfocusedContainerColor = ImmersiveCardInner,
                        focusedBorderColor = BlueLight,
                        unfocusedBorderColor = ImmersiveBorderSubtle,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = proteinText,
                    onValueChange = { proteinText = it },
                    label = { Text("Protein (g)", color = SlateTextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ImmersiveCardInner,
                        unfocusedContainerColor = ImmersiveCardInner,
                        focusedBorderColor = BlueLight,
                        unfocusedBorderColor = ImmersiveBorderSubtle,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = carbsText,
                    onValueChange = { carbsText = it },
                    label = { Text("Carbs (g)", color = SlateTextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ImmersiveCardInner,
                        unfocusedContainerColor = ImmersiveCardInner,
                        focusedBorderColor = BlueLight,
                        unfocusedBorderColor = ImmersiveBorderSubtle,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = fatsText,
                    onValueChange = { fatsText = it },
                    label = { Text("Fats (g)", color = SlateTextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ImmersiveCardInner,
                        unfocusedContainerColor = ImmersiveCardInner,
                        focusedBorderColor = BlueLight,
                        unfocusedBorderColor = ImmersiveBorderSubtle,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    val servings = servingCountText.toDoubleOrNull() ?: 1.0
                    val totalCal = caloriesText.toDoubleOrNull() ?: 0.0
                    val totalP = proteinText.toDoubleOrNull() ?: 0.0
                    val totalC = carbsText.toDoubleOrNull() ?: 0.0
                    val totalF = fatsText.toDoubleOrNull() ?: 0.0

                    val perServingCal = if (servings > 0) totalCal / servings else totalCal
                    val perServingP = if (servings > 0) totalP / servings else totalP
                    val perServingC = if (servings > 0) totalC / servings else totalC
                    val perServingF = if (servings > 0) totalF / servings else totalF

                    if (recipeName.isNotBlank()) {
                        onSaveRecipe(
                            recipeName,
                            "Custom Kitchen",
                            "Meals & Recipes",
                            1.0,
                            "serving",
                            perServingCal,
                            perServingP,
                            perServingC,
                            perServingF,
                            recipeIngredients
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_custom_recipe_button"),
                enabled = recipeName.isNotBlank()
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Recipe", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Recipe to Database", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
