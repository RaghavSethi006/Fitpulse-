package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.FitnessViewModel
import kotlinx.coroutines.launch

@Composable
fun GeminiApiKeyDialog(
    viewModel: FitnessViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    val customKey by viewModel.customGeminiApiKey.collectAsState()
    val hasActiveKey by viewModel.hasActiveGeminiKey.collectAsState()

    var inputKey by remember(customKey) { mutableStateOf(customKey) }
    var showKey by remember { mutableStateOf(false) }

    var isTesting by remember { mutableStateOf(false) }
    var testResult by remember { mutableStateOf<String?>(null) }
    var isTestSuccess by remember { mutableStateOf(false) }

    val isCustomActive = viewModel.apiKeyManager.isCustomKeyActive()
    val isBuildActive = viewModel.apiKeyManager.isBuildKeyActive()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
            border = BorderStroke(1.dp, ImmersiveBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("gemini_api_key_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(BluePrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VpnKey,
                                contentDescription = "Gemini API Key",
                                tint = BlueLight,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Gemini AI API Key",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary
                            )
                            Text(
                                text = "Power AI Coach & Food Scanner",
                                style = MaterialTheme.typography.labelSmall,
                                color = SlateTextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("close_api_key_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = SlateTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Active Key Status Banner
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasActiveKey) GreenAccent.copy(alpha = 0.1f) else EnergeticOrange.copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (hasActiveKey) GreenAccent.copy(alpha = 0.3f) else EnergeticOrange.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = if (hasActiveKey) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (hasActiveKey) GreenAccent else EnergeticOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = when {
                                    isCustomActive -> "Custom User Key Active"
                                    isBuildActive -> "Pre-configured Build Key Active"
                                    else -> "No API Key Configured"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (hasActiveKey) GreenAccent else EnergeticOrange
                            )
                            Text(
                                text = when {
                                    isCustomActive -> "Using your personal key saved on this device."
                                    isBuildActive -> "Using key configured during build / AI Studio."
                                    else -> "Enter your key below to activate all AI features."
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = SlateTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Enter your Google Gemini API key to customize your AI model access or avoid rate limits. Your key is stored securely on your local device.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SlateTextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Input Field
                OutlinedTextField(
                    value = inputKey,
                    onValueChange = {
                        inputKey = it
                        testResult = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("api_key_input_field"),
                    label = { Text("API Key (starts with AIza...)") },
                    placeholder = { Text("AIzaSy...") },
                    singleLine = true,
                    visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { showKey = !showKey },
                                modifier = Modifier.testTag("toggle_key_visibility")
                            ) {
                                Icon(
                                    imageVector = if (showKey) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (showKey) "Hide key" else "Show key",
                                    tint = SlateTextMuted
                                )
                            }
                            if (inputKey.isNotBlank()) {
                                IconButton(onClick = { inputKey = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear input",
                                        tint = SlateTextMuted
                                    )
                                }
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = ImmersiveBorder,
                        focusedLabelColor = BlueLight,
                        unfocusedLabelColor = SlateTextMuted,
                        focusedTextColor = SlateTextPrimary,
                        unfocusedTextColor = SlateTextPrimary,
                        cursorColor = BluePrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Paste & AI Studio link row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            clipboardManager.getText()?.text?.let { text ->
                                inputKey = text.trim()
                                testResult = null
                            }
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("paste_api_key_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = "Paste",
                            modifier = Modifier.size(16.dp),
                            tint = BlueLight
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Paste from Clipboard", style = MaterialTheme.typography.labelSmall, color = BlueLight)
                    }

                    TextButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://aistudio.google.com/app/apikey"))
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("get_api_key_link_button")
                    ) {
                        Text("Get Free Key ↗", style = MaterialTheme.typography.labelSmall, color = IndigoLight)
                    }
                }

                // Test Connection Status feedback
                if (testResult != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isTestSuccess) GreenAccent.copy(alpha = 0.12f) else ActiveCoral.copy(alpha = 0.12f)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isTestSuccess) GreenAccent.copy(alpha = 0.4f) else ActiveCoral.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isTestSuccess) Icons.Default.Check else Icons.Default.Error,
                                contentDescription = null,
                                tint = if (isTestSuccess) GreenAccent else ActiveCoral,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = testResult ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isTestSuccess) GreenAccent else ActiveCoral
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Test connection button
                    OutlinedButton(
                        onClick = {
                            val testTarget = inputKey.trim().ifBlank { viewModel.apiKeyManager.getActiveKey() }
                            if (testTarget.isBlank()) {
                                testResult = "Please enter an API key first."
                                isTestSuccess = false
                                return@OutlinedButton
                            }
                            isTesting = true
                            testResult = null
                            coroutineScope.launch {
                                val res = viewModel.testGeminiApiKey(testTarget)
                                isTesting = false
                                res.onSuccess {
                                    isTestSuccess = true
                                    testResult = "Key verified! Connected to Gemini Flash."
                                }.onFailure { error ->
                                    isTestSuccess = false
                                    testResult = error.message ?: "Verification failed."
                                }
                            }
                        },
                        enabled = !isTesting && (inputKey.isNotBlank() || hasActiveKey),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BluePrimary.copy(alpha = 0.5f)),
                        modifier = Modifier.weight(1f).testTag("test_api_key_button")
                    ) {
                        if (isTesting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = BlueLight
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Testing...", style = MaterialTheme.typography.labelSmall, color = BlueLight)
                        } else {
                            Icon(
                                imageVector = Icons.Default.NetworkCheck,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = BlueLight
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Test Key", style = MaterialTheme.typography.labelSmall, color = BlueLight)
                        }
                    }

                    // Save Button
                    Button(
                        onClick = {
                            val trimmed = inputKey.trim()
                            if (trimmed.isNotBlank()) {
                                viewModel.saveGeminiApiKey(trimmed)
                            }
                            onDismiss()
                        },
                        enabled = inputKey.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.weight(1f).testTag("save_api_key_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Key", style = MaterialTheme.typography.labelMedium)
                    }
                }

                // Reset to default button (if custom key is configured)
                if (isCustomActive) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = {
                            viewModel.clearGeminiApiKey()
                            inputKey = ""
                            testResult = null
                        },
                        modifier = Modifier.fillMaxWidth().testTag("clear_custom_key_button")
                    ) {
                        Text(
                            text = "Remove Custom Key (Reset to Default)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SlateTextMuted
                        )
                    }
                }
            }
        }
    }
}
