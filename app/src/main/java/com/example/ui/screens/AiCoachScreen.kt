package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.data.PrepopulatedData
import com.example.ui.components.GeminiApiKeyDialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiCoachScreen(viewModel: FitnessViewModel) {
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isThinking by viewModel.isCoachThinking.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val todayLogs by viewModel.todayMealLogs.collectAsState()
    val hasActiveKey by viewModel.hasActiveGeminiKey.collectAsState()

    val user = userProfile ?: PrepopulatedData.defaultProfile
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var showApiKeyDialog by remember { mutableStateOf(false) }

    if (showApiKeyDialog) {
        GeminiApiKeyDialog(
            viewModel = viewModel,
            onDismiss = { showApiKeyDialog = false }
        )
    }

    val quickQuestions = listOf(
        "How do I break through a bench press plateau?",
        "What is the optimal post-workout meal for my macros?",
        "How to prevent shoulder impingement on overhead presses?",
        "How should I structure my deload week?",
        "Should I do cardio before or after lifting for ${user.goal}?"
    )

    // Scroll to bottom when messages update
    LaunchedEffect(chatMessages.size, isThinking) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(IndigoAccent, PurpleAccent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "AI Coach",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Coach Alex (AI Mentor)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary
                    )
                    Text(
                        text = "Real-time science-based guidance",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateTextSecondary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { showApiKeyDialog = true },
                    modifier = Modifier.testTag("ai_coach_api_key_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.VpnKey,
                        contentDescription = "Configure Gemini API Key",
                        tint = if (hasActiveKey) GreenAccent else EnergeticOrange
                    )
                }

                IconButton(onClick = { viewModel.clearCoachChat() }) {
                    Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Clear Chat", tint = SlateTextSecondary)
                }
            }
        }

        if (!hasActiveKey) {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = EnergeticOrange.copy(alpha = 0.12f)),
                border = BorderStroke(1.dp, EnergeticOrange.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showApiKeyDialog = true }
                    .testTag("api_key_missing_banner")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = null,
                        tint = EnergeticOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Add Your Gemini API Key",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = EnergeticOrange
                        )
                        Text(
                            text = "Tap to enter your free key from Google AI Studio and chat with Coach Alex.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateTextSecondary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = EnergeticOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Context Badge
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ImmersiveCard,
            border = BorderStroke(1.dp, ImmersiveBorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Tune, contentDescription = "Context", tint = BlueLight, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Context: ${user.goal} • Split: ${user.workoutSplit} • ${todayLogs.sumOf { it.calories }.toInt()}/${user.dailyCalorieTarget} kcal",
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chat messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (chatMessages.isEmpty()) {
                item {
                    CoachWelcomeCard(user.name, user.goal)
                }
            }

            items(chatMessages) { msg ->
                ChatMessageBubble(message = msg)
            }

            if (isThinking) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = ImmersiveCard,
                            border = BorderStroke(1.dp, ImmersiveBorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = IndigoLight, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Coach Alex is analyzing...", style = MaterialTheme.typography.bodySmall, color = IndigoLight)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Suggestions Horizontal Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(quickQuestions) { q ->
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = ImmersiveCard,
                    border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                    modifier = Modifier.clickable {
                        inputText = q
                        viewModel.sendCoachMessage(q)
                        inputText = ""
                    }
                ) {
                    Text(
                        text = q,
                        fontSize = 11.sp,
                        color = SlateTextSecondary,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Input Field and Send Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 90.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask about form, nutrition, routines...", color = SlateTextMuted) },
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ImmersiveCard,
                    unfocusedContainerColor = ImmersiveCard,
                    focusedBorderColor = BlueLight,
                    unfocusedBorderColor = ImmersiveBorderSubtle,
                    focusedTextColor = SlateTextPrimary,
                    unfocusedTextColor = SlateTextPrimary
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_coach_input"),
                maxLines = 3
            )

            FloatingActionButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        val text = inputText
                        inputText = ""
                        viewModel.sendCoachMessage(text)
                    }
                },
                containerColor = BlueVibrant,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(50.dp)
                    .testTag("send_coach_message_button")
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun CoachWelcomeCard(userName: String, goal: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveCard),
        border = BorderStroke(1.dp, ImmersiveBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = "Welcome", tint = IndigoLight)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Welcome to FitPulse Coaching, $userName!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "I'm Coach Alex, your 24/7 AI fitness and nutrition mentor. I have full context on your target of '$goal', your daily macro logs, and your gym routines.\n\nAsk me about exercise techniques, breaking plateaus, customized meal timing, or injury prevention.",
                style = MaterialTheme.typography.bodyMedium,
                color = SlateTextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage) {
    val isUser = message.role == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = if (isUser) 20.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 20.dp
            ),
            color = if (isUser) BlueVibrant else ImmersiveCard,
            border = if (isUser) null else BorderStroke(1.dp, ImmersiveBorderSubtle),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (!isUser) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "Coach",
                            tint = IndigoLight,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Coach Alex",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = IndigoLight
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlateTextPrimary,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
