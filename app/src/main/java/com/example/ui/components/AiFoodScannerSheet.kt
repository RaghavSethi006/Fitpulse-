package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FoodItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiFoodScannerSheet(
    isScanning: Boolean,
    scannedFood: FoodItem?,
    scanError: String?,
    onScanRequest: (Bitmap?, String?) -> Unit,
    onConfirmLogScannedFood: (FoodItem, String) -> Unit,
    onDismiss: () -> Unit
) {
    var textQuery by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("LUNCH") }
    val mealTypes = listOf("BREAKFAST", "LUNCH", "DINNER", "SNACKS")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = ImmersiveCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = BluePrimary.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, BlueLight.copy(alpha = 0.2f)),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.DocumentScanner,
                                contentDescription = "AI Scanner",
                                tint = BlueLight
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "AI Smart Food Scanner",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = "Powered by Gemini Vision & Nutritionist",
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateTextSecondary
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = SlateTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Text/Description Scanner Input with Quick Prompt suggestions
            OutlinedTextField(
                value = textQuery,
                onValueChange = { textQuery = it },
                label = { Text("Describe dish, label, or paste barcode info", color = SlateTextSecondary) },
                placeholder = { Text("e.g. 2 fried eggs, 2 slices avocado sourdough, 1 iced latte with oat milk", color = SlateTextMuted) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_food_input"),
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

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Example Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "Steak Plate" to "Grilled ribeye steak 200g with mashed potatoes and garlic asparagus",
                    "Acai Bowl" to "Acai bowl with banana, granola, chia seeds, and peanut butter drizzle",
                    "Protein Shake" to "2 scoops whey isolate, 1 banana, 300ml whole milk shake"
                ).forEach { (label, prompt) ->
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = ImmersiveCardInner,
                        border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                        modifier = Modifier.clickable { textQuery = prompt }
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            color = SlateTextSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onScanRequest(null, textQuery) },
                enabled = textQuery.isNotBlank() && !isScanning,
                colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("scan_food_button")
            ) {
                if (isScanning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Analyzing Ingredients with AI...", color = Color.White, fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Scan", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Analyze Food & Macros", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            // Error display
            scanError?.let { err ->
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color(0x33EF4444),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0x66EF4444)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = err,
                        color = Color(0xFFFCA5A5),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Scanned Food Result Card
            scannedFood?.let { food ->
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveCardInner),
                    border = BorderStroke(1.dp, ImmersiveBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = food.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateTextPrimary
                                )
                                Text(
                                    text = "Estimated Portion: ${food.servingSize.toInt()} ${food.servingUnit}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SlateTextSecondary
                                )
                            }

                            Text(
                                text = "${food.calories.toInt()} kcal",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = EnergeticOrange
                            )
                        }

                        if (food.recipeIngredients.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Ingredients: ${food.recipeIngredients}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Macro Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            MacroPill("Protein", "${food.protein.toInt()}g", ProteinBlue)
                            MacroPill("Carbs", "${food.carbs.toInt()}g", CarbsAmber)
                            MacroPill("Fats", "${food.fats.toInt()}g", FatRose)
                            if (food.fiber > 0) {
                                MacroPill("Fiber", "${food.fiber.toInt()}g", BlueLight)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Meal Type Selection for logging
                        Text(
                            text = "Log To Meal:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SlateTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            mealTypes.forEach { type ->
                                val isSelected = selectedMealType == type
                                Surface(
                                    shape = RoundedCornerShape(100.dp),
                                    color = if (isSelected) BluePrimary.copy(alpha = 0.2f) else ImmersiveCard,
                                    border = BorderStroke(1.dp, if (isSelected) BlueLight else ImmersiveBorderSubtle),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedMealType = type }
                                ) {
                                    Text(
                                        text = type.lowercase().replaceFirstChar { it.uppercase() },
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) BlueLight else SlateTextSecondary,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onConfirmLogScannedFood(food, selectedMealType)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BlueVibrant),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("confirm_scanned_food_button")
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "Confirm", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add ${food.calories.toInt()} kcal to $selectedMealType", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MacroPill(name: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f)),
        modifier = Modifier.padding(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = name, style = MaterialTheme.typography.labelSmall, color = color)
            Text(text = value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
