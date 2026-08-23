# FitPulse — AI-Powered Fitness & Nutrition Ecosystem ⚡🥗🏋️

[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20Jetpack%20Compose-3DDC84?logo=android&logoColor=white)](#)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?logo=kotlin&logoColor=white)](#)
[![AI Engine](https://img.shields.io/badge/AI%20Engine-Google%20Gemini%20Flash-4285F4?logo=google&logoColor=white)](#)
[![Persistence](https://img.shields.io/badge/Database-Room%20(SQLite)-4285F4?logo=sqlite&logoColor=white)](#)
[![Design](https://img.shields.io/badge/Design%20System-Material%203%20Immersive%20Dark-FF6F00?logo=materialdesign&logoColor=white)](#)

---

## 📖 Table of Contents

1. [Overview](#-overview)
2. [Key Feature Modules](#-key-feature-modules)
   - [Unified Analytics Dashboard](#1-unified-analytics-dashboard)
   - [Nutrition Tracker & Food Engine](#2-nutrition-tracker--food-engine)
   - [AI Smart Food Scanner (Gemini Vision)](#3-ai-smart-food-scanner-gemini-vision)
   - [Gym Routine Hub & Split Architect](#4-gym-routine-hub--split-architect)
   - [Active Workout Session & 2D Animated Guide](#5-active-workout-session--2d-animated-guide)
   - [Android TTS Voice Coach](#6-android-tts-voice-coach)
   - [AI Nutritionist & Weekly Meal Planner](#7-ai-nutritionist--weekly-meal-planner)
   - [AI Coach Alex (Conversational Assistant)](#8-ai-coach-alex-conversational-assistant)
3. [Architecture & Design System](#-architecture--design-system)
   - [MVVM + Repository Pattern](#mvvm--repository-pattern)
   - [Dark Immersive Aesthetic & Theme Tokens](#dark-immersive-aesthetic--theme-tokens)
   - [Canvas Graphics & Dynamic Visualizations](#canvas-graphics--dynamic-visualizations)
4. [Database & Local Persistence](#-database--local-persistence)
   - [Room Schema & Entities](#room-schema--entities)
   - [Pre-Seeded 30-Day Simulation Dataset](#pre-seeded-30-day-simulation-dataset)
5. [Tech Stack & Dependencies](#-tech-stack--dependencies)
6. [Project Structure](#-project-structure)
7. [Environment & API Configuration](#-environment--api-configuration)
8. [Build & Installation](#-build--installation)

---

## 🌟 Overview

**FitPulse** is an all-in-one fitness, nutrition, and workout companion built natively for Android using **Kotlin** and **Jetpack Compose**. Designed with an **Immersive Dark UI**, FitPulse unifies every pillar of personal fitness into a seamless, offline-first experience enhanced by **Google Gemini AI**.

Whether logging daily macros, executing high-intensity gym splits with interactive 2D animated exercise cues and voice encouragement, analyzing complex meal plates via multimodal AI vision, or generating tailored 7-day nutritional roadmaps, FitPulse provides real-time insights with zero bloat.

---

## 🚀 Key Feature Modules

```
                    ┌──────────────────────────────────────────────┐
                    │               FITPULSE ECOSYSTEM             │
                    └──────────────────────┬───────────────────────┘
                                           │
         ┌──────────────────┬──────────────┴─────┬──────────────────┐
         ▼                  ▼                    ▼                  ▼
┌─────────────────┐┌─────────────────┐┌──────────────────┐┌──────────────────┐
│    Dashboard    ││ Nutrition Engine││ Gym Routines Hub ││ AI Health Suite  │
│ • Macro Rings   ││ • 100+ Food DB  ││ • Custom Splits  ││ • Gemini Scanner │
│ • Weight Trends ││ • Custom Recipes││ • 2D Form Guide  ││ • Meal Planner   │
│ • Hydration Log ││ • Date History  ││ • Voice Coach    ││ • AI Coach Alex  │
└─────────────────┘└─────────────────┘└──────────────────┘└──────────────────┘
```

### 1. Unified Analytics Dashboard
- **Circular Calorie & Macro Gauges**: Displays live calorie balance (Consumed vs. Burned vs. Net Goal) with custom gradient sweeps and individual progress cards for Protein, Carbs, and Fats.
- **30-Day Weight Trend Analytics**: Interactive line chart plotting historical body weight check-ins against target milestones with dynamic min/max scaling.
- **7-Day Calorie Intake Bar Chart**: Visualizes weekly intake trends with target goal threshold lines and day-by-day status indicators.
- **One-Tap Quick Actions**: Instant access to Log Food, Scan Plate, Start Routine, and Hydration logging (+250ml / +500ml / Reset).
- **Workout Streak & Activity Feed**: Real-time display of weekly workout consistency, total volume lifted (kg), and active training minutes.

### 2. Nutrition Tracker & Food Engine
- **Pre-Loaded Nutritional Library**: Over 100+ verified foods, whole ingredients, and staple brands categorized by Protein, Carbs & Grains, Dairy & Eggs, Fruits & Veggies, Fats, Snacks, and Meals.
- **Custom Recipe Builder**: Create and store multi-ingredient recipes with serving calculators and automatic per-portion macro distribution.
- **Time-Traveling Date Strip**: Seamlessly jump across any historical date or today with real-time Room Flow updates.
- **Meal Categorization**: Log entries into structured categories: **Breakfast**, **Lunch**, **Dinner**, and **Snacks**, with per-meal calorie totals and item-level deletion.

### 3. AI Smart Food Scanner (Gemini Vision)
- **Multimodal Nutrition Analysis**: Powered by Gemini (`gemini-2.5-flash`), allows users to snap/upload meal photos or type complex natural language descriptions (e.g., *"Grilled ribeye steak 200g with mashed sweet potatoes and asparagus"*).
- **Structured Macro Extraction**: Parses estimated portions, calories, protein, carbohydrates, fats, dietary fiber, and ingredient lists into a confirmable log sheet.
- **Single-Tap Log & Allocate**: Review AI estimates and immediately log to any meal category in the user's daily journal.

### 4. Gym Routine Hub & Split Architect
- **Curated Pre-Loaded Splits**:
  - *Push Day: Hypertrophy & Power* (Chest, Shoulders, Triceps)
  - *Pull Day: Back & Biceps Strength* (Lats, Upper Back, Biceps)
  - *Legs & Core Destruction* (Quads, Hamstrings, Calves, Abs)
  - *Full Body Athletic Conditioning* (Functional strength & endurance)
- **Custom Routine Builder**: Add custom routines with assigned days of the week, target durations, and exercise sequencing.
- **Exercise Library**: 50+ compound and isolation exercises with muscle group tags, equipment requirements, and technique cues.

### 5. Active Workout Session & 2D Animated Guide
- **Live Interactive Workout Mode**: Step-by-step exercise execution view with set-by-set completion checkboxes, reps, and load tracking.
- **Interactive 2D Skeletal Animation**: Custom Jetpack Compose `Canvas` rendering continuous 2D biomechanical motion loops for each exercise (e.g., Bench Press barbell descent/press, Squat hip hinge/knee bend, Pull-up ascent/descent, Deadlift bar path).
- **Automated Rest Countdown Timer**: Configurable rest interval timer with visual circular countdown, audio alerts, and skip/add 30s controls.
- **Workout Summary & Auto-Logging**: Calculates total training duration, total volume lifted ($\text{kg} = \text{sets} \times \text{reps} \times \text{weight}$), and estimated calories burned, archiving to workout history.

### 6. Android TTS Voice Coach
- **On-Device Speech Synthesis**: Integrated with `android.speech.tts.TextToSpeech`.
- **Audio Coaching Cues**: Announces upcoming sets, rep targets, rest timer completions, form reminders, and motivational feedback throughout the active workout session without requiring the user to look at the screen.

### 7. AI Nutritionist & Weekly Meal Planner
- **TDEE & Macro Target Calibration**: Calculates Basal Metabolic Rate (Mifflin-St Jeor formula) and Total Daily Energy Expenditure based on user gender, age, height, weight, activity level, and primary fitness goal (**Cut / Fat Loss**, **Maintain / Recomp**, **Clean Bulk**).
- **7-Day Meal Plan Generator**: Gemini AI generates structured, culturally adaptable, high-protein daily meal schedules (Breakfast, Lunch, Dinner, Snack) calibrated to exact target macros.
- **Single-Tap Adoption**: Save AI meal plans directly to the local Room database or adopt individual recipes directly into the food log.

### 8. AI Coach Alex (Conversational Assistant)
- **Domain-Specific Sports Science AI**: Built-in conversational fitness coach with deep understanding of progressive overload, nutrient timing, hypertrophy volume, recovery protocols, and supplement safety.
- **Persistent Chat History**: All conversations are stored in Room and preserved across sessions.
- **Interactive Quick-Prompt Badges**: One-tap conversation starters for common fitness questions (e.g., *"How much protein per kg?"*, *"Creatine timing"*, *"Rest intervals for strength"*).

---

## 🎨 Architecture & Design System

### MVVM + Repository Pattern

```
┌──────────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                          │
│   Jetpack Compose UI (Screens, Canvas Animations, Components)     │
└─────────────────────────────────▲────────────────────────────────┘
                                  │ UI State / Events
┌─────────────────────────────────┴────────────────────────────────┐
│                       VIEWMODEL LAYER                            │
│           FitnessViewModel (StateFlow, Coroutines, TTS)          │
└─────────────────────────────────▲────────────────────────────────┘
                                  │ Flow / Suspend Functions
┌─────────────────────────────────┴────────────────────────────────┐
│                       REPOSITORY LAYER                           │
│           FitPulseRepository (Data aggregation & caching)         │
└───────────────────▲──────────────────────────────▲───────────────┘
                    │                              │
┌───────────────────┴──────────────┐ ┌─────────────┴───────────────┐
│          LOCAL STORAGE           │ │         REMOTE AI           │
│  FitPulseDatabase (Room SQLite)  │ │ GeminiService (REST Client) │
│  • Daos: User, Meals, Routines,  │ │ • Multimodal Vision / Chat  │
│    Workouts, Weight, Chat        │ │ • Meal Plan JSON Generator  │
└──────────────────────────────────┘ └─────────────────────────────┘
```

### Dark Immersive Aesthetic & Theme Tokens

The application employs an **Immersive Dark Design System** engineered for OLED displays and high-contrast legibility:

| Token Name | Hex Code | Purpose |
|:---|:---|:---|
| `ImmersiveDark` | `#0F1115` | Deep main background canvas |
| `ImmersiveCard` | `#1A1D23` | Primary elevated card container |
| `ImmersiveCardInner` | `#222630` | Secondary inner surface & input field container |
| `ImmersiveBorder` | `#2A2E39` | Subtle structural border line |
| `BluePrimary` | `#3B82F6` | Primary action accent & protein macro badge |
| `BlueVibrant` | `#2563EB` | Active button fill & primary interactive elements |
| `EnergeticOrange`| `#F97316` | Calories burned & primary highlight metric |
| `CarbsAmber` | `#F59E0B` | Carbohydrate macro badge & energy indicator |
| `FatRose` | `#F43F5E` | Dietary fats macro badge & intensity indicator |
| `EmeraldVibrant` | `#10B981` | Completed sets, hydration progress & recovery |

### Canvas Graphics & Dynamic Visualizations
- **Smooth Arc Gauges**: Custom `drawArc` computations with anti-aliasing and cap rounding for macro progress rings.
- **Biomechanical 2D Skeletal Animation**: Procedural trigonometry calculations for real-time limb movement during exercise repetitions.
- **Adaptive Trend Charts**: Custom line and bar charts with linear gradient fills and baseline references.

---

## 🗄️ Database & Local Persistence

FitPulse is built with an **offline-first architecture** using Android Jetpack Room with SQLite under the hood.

### Room Schema & Entities

1. **`user_profile`**: Single-row configuration storing weight, height, age, activity level, calorie/macro targets, and water intake.
2. **`food_items`**: Nutritional dictionary containing names, brands, categories, portion units, calories, macros, and custom recipe tags.
3. **`meal_logs`**: Historical and live meal entries linked by date (`YYYY-MM-DD`) and meal category (`BREAKFAST`, `LUNCH`, `DINNER`, `SNACKS`).
4. **`workout_routines`**: Workout split definitions with routine metadata and serialized JSON exercise configurations (`RoutineExercise`).
5. **`workout_history`**: Completed workout logs capturing duration, volume (kg), calories burned, and completion timestamps.
6. **`weekly_meal_plan`**: 7-day structured meal planner entries with macro distributions.
7. **`weight_logs`**: Time-stamped body weight entries with progressive trend notes.
8. **`chat_messages`**: Conversational history between the user and AI Coach Alex.

### Pre-Seeded 30-Day Simulation Dataset

Upon initial launch, the database automatically populates a rich **1-month historical timeline + present date dataset**:
- **30 Days of Complete Meal Logs**: Rotating real-food daily nutrition plans leading up to active today logs.
- **11 Weight Checkpoints**: Documenting a gradual, realistic body recomposition from 78.6 kg to 76.5 kg.
- **18 Logged Workout Sessions**: Full workout histories detailing volume, time, and calories across Push, Pull, Leg, and Conditioning splits.
- **Active Dialogue History**: Contextual sports science Q&A threads with AI Coach Alex.

---

## 🛠️ Tech Stack & Dependencies

- **Language**: Kotlin 2.0+
- **UI Framework**: Jetpack Compose (BOM 2024.10.01) with Material 3
- **Architecture Components**:
  - `androidx.lifecycle:lifecycle-viewmodel-compose`
  - `androidx.lifecycle:lifecycle-runtime-compose`
  - `androidx.navigation:navigation-compose`
- **Database / Local Persistence**:
  - `androidx.room:room-runtime:2.6.1`
  - `androidx.room:room-ktx:2.6.1`
  - Kotlin Symbol Processing (`KSP`)
- **Networking & Serialization**:
  - `com.squareup.okhttp3:okhttp:4.12.0`
  - `com.squareup.moshi:moshi-kotlin:1.15.1`
- **AI / LLM Integration**: Google Gemini Developer REST API (`gemini-2.5-flash`)
- **Voice & Speech**: Android Native `android.speech.tts.TextToSpeech`
- **Asynchronous Concurrency**: Kotlin Coroutines (`kotlinx-coroutines-android`) & Flow

---

## 📁 Project Structure

```
FitPulse/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt               # Main entry point & bottom navigation
│   │   │   │   ├── data/
│   │   │   │   │   ├── Entities.kt               # Room database entity models
│   │   │   │   │   ├── Daos.kt                   # Room Data Access Objects
│   │   │   │   │   ├── FitPulseDatabase.kt       # Room Database configuration & migrations
│   │   │   │   │   ├── FitPulseRepository.kt     # Single source of truth repository
│   │   │   │   │   └── PrepopulatedData.kt       # 100+ foods, routines & 30-day simulator
│   │   │   │   ├── network/
│   │   │   │   │   └── GeminiService.kt          # Gemini AI REST client (Vision, Plan, Coach)
│   │   │   │   ├── tts/
│   │   │   │   │   └── VoiceCoachManager.kt      # Text-to-Speech audio engine
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/
│   │   │   │   │   │   ├── ActiveWorkoutSessionView.kt  # Live gym execution & rest timer
│   │   │   │   │   │   ├── AiFoodScannerSheet.kt        # Gemini multimodal camera sheet
│   │   │   │   │   │   ├── ExerciseAnimationCanvas.kt   # 2D animated biomechanical guide
│   │   │   │   │   │   ├── FoodSearchAndLogDialog.kt    # Database search & custom recipe
│   │   │   │   │   │   ├── MacroProgressComponents.kt   # Dynamic circular gauges & bars
│   │   │   │   │   │   ├── RoutineBuilderDialog.kt      # Split builder & exercise selector
│   │   │   │   │   │   └── UserProfileDialog.kt         # TDEE & macro calibration
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── DashboardScreen.kt           # Central hub & analytics charts
│   │   │   │   │   │   ├── NutritionScreen.kt           # Meal journal & food logging
│   │   │   │   │   │   ├── GymRoutinesScreen.kt         # Routines, splits & workout history
│   │   │   │   │   │   ├── AiNutritionistScreen.kt      # Weekly planner & TDEE advisor
│   │   │   │   │   │   └── AiCoachScreen.kt             # Conversational fitness assistant
│   │   │   │   │   ├── theme/
│   │   │   │   │   │   ├── Color.kt                     # Immersive Dark palette tokens
│   │   │   │   │   │   ├── Theme.kt                     # Material 3 dark color scheme
│   │   │   │   │   │   └── Type.kt                      # Typography configurations
│   │   │   │   │   └── viewmodel/
│   │   │   │   │       └── FitnessViewModel.kt          # Central state & business logic
│   │   │   └── res/
│   │   │       ├── values/
│   │   │       │   ├── strings.xml
│   │   │       │   └── themes.xml
│   │   │       └── mipmap-...
│   │   └── build.gradle.kts
│   └── build.gradle.kts
├── settings.gradle.kts
├── metadata.json
└── README.md
```

---

## 🔑 Environment & API Configuration

FitPulse uses Google Gemini for real-time food plate vision, meal planning, and coaching.

1. **Obtain a Gemini API Key**: Generate a free API key from [Google AI Studio](https://aistudio.google.com/).
2. **Configure in AI Studio / Secrets**:
   - In Google AI Studio Build, enter your key in the **Secrets Panel** under `GEMINI_API_KEY`.
   - Alternatively, for local builds, add the key into your `.env` file:
     ```env
     GEMINI_API_KEY=your_actual_gemini_api_key_here
     ```
3. The Gradle Secrets Plugin automatically injects `BuildConfig.GEMINI_API_KEY` at compile time without exposing sensitive keys in version control.

---

## 💻 Build & Installation

### Prerequisites
- Android Studio Ladybug (2024.2+) or higher
- JDK 17 or JDK 21
- Android SDK 34+ (Android 14 / Upside Down Cake)

### Build Commands
To compile and assemble the debug APK:
```bash
gradle :app:assembleDebug
```

To run unit and local database tests:
```bash
gradle :app:testDebugUnitTest
```

---

## 🔒 Privacy & Local-First Philosophy
All personal telemetry, weight logs, food journals, and workout histories are saved strictly to the on-device **Room SQLite Database**. Network requests are only made when explicitly triggering Gemini AI features (Food Vision Scanning, AI Meal Plan generation, or Chat with Coach Alex).

---

*FitPulse — Designed and engineered for peak human performance.* ⚡💪
