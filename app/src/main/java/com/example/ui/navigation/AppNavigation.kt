package com.example.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.PrepopulatedData
import com.example.ui.components.ActiveWorkoutSessionView
import com.example.ui.components.UserProfileDialog
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ActiveWorkoutState
import com.example.ui.viewmodel.FitnessViewModel

enum class NavigationTab(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    DASHBOARD("dashboard", "HOME", Icons.Filled.Home, Icons.Outlined.Home),
    NUTRITION("nutrition", "LOGS", Icons.Filled.Restaurant, Icons.Outlined.Restaurant),
    GYM("gym", "WORKOUT", Icons.Filled.FitnessCenter, Icons.Outlined.FitnessCenter),
    NUTRITIONIST("nutritionist", "AI DIET", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    COACH("coach", "COACH", Icons.Filled.SmartToy, Icons.Outlined.SmartToy)
}

@Composable
fun FitPulseMainApp(viewModel: FitnessViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavigationTab.DASHBOARD.route

    val activeRoutine by viewModel.activeRoutine.collectAsState()
    val workoutState by viewModel.workoutState.collectAsState()
    val activeExercisesList by viewModel.activeExercisesList.collectAsState()
    val currentIndex by viewModel.currentExerciseIndex.collectAsState()
    val currentSet by viewModel.currentSetIndex.collectAsState()
    val timerRemaining by viewModel.timerSecondsRemaining.collectAsState()
    val elapsedSeconds by viewModel.totalWorkoutSecondsElapsed.collectAsState()
    val allExercises by viewModel.allExercises.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    var showProfileDialog by remember { mutableStateOf(false) }

    val isWorkoutActive = activeRoutine != null && workoutState != ActiveWorkoutState.IDLE

    Scaffold(
        containerColor = ImmersiveBackground,
        bottomBar = {
            if (!isWorkoutActive) {
                Surface(
                    color = ImmersiveNav,
                    tonalElevation = 8.dp,
                    border = BorderStroke(1.dp, ImmersiveBorder)
                ) {
                    NavigationBar(
                        containerColor = ImmersiveNav,
                        contentColor = BlueLight,
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .testTag("main_bottom_nav")
                            .navigationBarsPadding()
                    ) {
                        NavigationTab.values().forEach { tab ->
                            val isSelected = currentRoute == tab.route
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    if (currentRoute != tab.route) {
                                        navController.navigate(tab.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        letterSpacing = 0.5.sp
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BlueLight,
                                    selectedTextColor = BlueLight,
                                    unselectedIconColor = SlateTextMuted,
                                    unselectedTextColor = SlateTextMuted,
                                    indicatorColor = BluePrimary.copy(alpha = 0.18f)
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationTab.DASHBOARD.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(NavigationTab.DASHBOARD.route) {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToNutrition = { navController.navigate(NavigationTab.NUTRITION.route) },
                    onNavigateToGym = { navController.navigate(NavigationTab.GYM.route) },
                    onNavigateToAiNutritionist = { navController.navigate(NavigationTab.NUTRITIONIST.route) },
                    onNavigateToAiCoach = { navController.navigate(NavigationTab.COACH.route) },
                    onOpenProfile = { showProfileDialog = true }
                )
            }

            composable(NavigationTab.NUTRITION.route) {
                NutritionScreen(viewModel = viewModel)
            }

            composable(NavigationTab.GYM.route) {
                GymRoutinesScreen(viewModel = viewModel)
            }

            composable(NavigationTab.NUTRITIONIST.route) {
                AiNutritionistScreen(viewModel = viewModel)
            }

            composable(NavigationTab.COACH.route) {
                AiCoachScreen(viewModel = viewModel)
            }
        }
    }

    // Fullscreen Active Workout Session Overlay
    if (isWorkoutActive && activeRoutine != null) {
        ActiveWorkoutSessionView(
            routine = activeRoutine!!,
            exercisesList = activeExercisesList,
            currentIndex = currentIndex,
            currentSet = currentSet,
            workoutState = workoutState,
            timerSecondsRemaining = timerRemaining,
            totalElapsedSeconds = elapsedSeconds,
            exerciseLibrary = allExercises,
            voiceCoach = viewModel.voiceCoach,
            onCompleteSet = { viewModel.completeCurrentSet() },
            onSkipRest = { viewModel.skipRestTimer() },
            onFinishWorkout = { viewModel.finishWorkout() },
            onCancelWorkout = { viewModel.cancelActiveWorkout() }
        )
    }

    // User Profile Dialog
    if (showProfileDialog) {
        UserProfileDialog(
            currentProfile = userProfile ?: PrepopulatedData.defaultProfile,
            onSaveProfile = { updated ->
                viewModel.updateProfile(updated)
                showProfileDialog = false
            },
            onDismiss = { showProfileDialog = false }
        )
    }
}
