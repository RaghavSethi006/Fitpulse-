package com.example.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.text.SimpleDateFormat
import java.util.*

object PrepopulatedData {

    val defaultProfile = UserProfile(
        id = 1,
        name = "Alex Vance",
        age = 26,
        gender = "Male",
        heightCm = 178.0,
        weightKg = 76.5,
        targetWeightKg = 73.0,
        activityLevel = "Moderately Active",
        goal = "Muscle Building & Fat Loss",
        experienceLevel = "Intermediate",
        workoutSplit = "Push / Pull / Legs",
        equipment = "Full Gym",
        dietaryPreference = "High Protein",
        medicalNotes = "Keep strict form on lower back during deadlifts.",
        dailyCalorieTarget = 2450,
        proteinTargetGrams = 170,
        carbsTargetGrams = 250,
        fatsTargetGrams = 70,
        waterTargetMl = 3200
    )

    val starterFoods = listOf(
        // Proteins
        FoodItem(name = "Chicken Breast (Cooked, Skinless)", brand = "Fresh Farm", category = "Protein", servingSize = 100.0, servingUnit = "g", calories = 165.0, protein = 31.0, carbs = 0.0, fats = 3.6, fiber = 0.0),
        FoodItem(name = "Whey Protein Isolate (Vanilla/Choc)", brand = "Optimum Nutrition", category = "Protein", servingSize = 1.0, servingUnit = "scoop (32g)", calories = 120.0, protein = 24.0, carbs = 3.0, fats = 1.0, fiber = 0.0),
        FoodItem(name = "Atlantic Salmon Fillet (Grilled)", brand = "Wild Catch", category = "Protein", servingSize = 150.0, servingUnit = "g", calories = 310.0, protein = 34.0, carbs = 0.0, fats = 18.0, fiber = 0.0),
        FoodItem(name = "Lean Ground Beef (90/10)", brand = "Butcher's Choice", category = "Protein", servingSize = 100.0, servingUnit = "g", calories = 176.0, protein = 26.0, carbs = 0.0, fats = 10.0, fiber = 0.0),
        FoodItem(name = "Canned Tuna in Water (Drained)", brand = "Starkist", category = "Protein", servingSize = 1.0, servingUnit = "can (120g)", calories = 130.0, protein = 29.0, carbs = 0.0, fats = 1.0, fiber = 0.0),
        FoodItem(name = "Firm Tofu (Organic)", brand = "House Foods", category = "Protein", servingSize = 100.0, servingUnit = "g", calories = 83.0, protein = 10.0, carbs = 2.0, fats = 5.0, fiber = 1.0),
        FoodItem(name = "Turkey Breast Slices", brand = "Applegate", category = "Protein", servingSize = 60.0, servingUnit = "g", calories = 60.0, protein = 12.0, carbs = 1.0, fats = 1.0, fiber = 0.0),
        FoodItem(name = "Plant Protein Powder (Pea & Rice)", brand = "Orgain", category = "Protein", servingSize = 1.0, servingUnit = "scoop (35g)", calories = 140.0, protein = 21.0, carbs = 7.0, fats = 3.0, fiber = 2.0),
        FoodItem(name = "Shrimp (Boiled/Grilled)", brand = "Ocean Fresh", category = "Protein", servingSize = 100.0, servingUnit = "g", calories = 99.0, protein = 24.0, carbs = 0.2, fats = 0.3, fiber = 0.0),
        FoodItem(name = "Pork Tenderloin (Roasted)", brand = "Prairie Fresh", category = "Protein", servingSize = 100.0, servingUnit = "g", calories = 143.0, protein = 26.0, carbs = 0.0, fats = 3.5, fiber = 0.0),

        // Dairy & Eggs
        FoodItem(name = "Whole Large Egg", brand = "Vital Farms", category = "Dairy & Eggs", servingSize = 1.0, servingUnit = "piece", calories = 72.0, protein = 6.3, carbs = 0.4, fats = 4.8, fiber = 0.0),
        FoodItem(name = "Egg Whites", brand = "Organic Valley", category = "Dairy & Eggs", servingSize = 100.0, servingUnit = "g", calories = 52.0, protein = 11.0, carbs = 0.7, fats = 0.2, fiber = 0.0),
        FoodItem(name = "Greek Yogurt 0% Fat (Plain)", brand = "Chobani / Fage", category = "Dairy & Eggs", servingSize = 170.0, servingUnit = "g (1 cup)", calories = 100.0, protein = 18.0, carbs = 6.0, fats = 0.0, fiber = 0.0),
        FoodItem(name = "Cottage Cheese (Low Fat 2%)", brand = "Good Culture", category = "Dairy & Eggs", servingSize = 113.0, servingUnit = "g (1/2 cup)", calories = 90.0, protein = 14.0, carbs = 4.0, fats = 2.5, fiber = 0.0),
        FoodItem(name = "Almond Milk (Unsweetened)", brand = "Silk", category = "Dairy & Eggs", servingSize = 240.0, servingUnit = "ml (1 cup)", calories = 30.0, protein = 1.0, carbs = 1.0, fats = 2.5, fiber = 1.0),
        FoodItem(name = "Skim Milk (Fat-Free)", brand = "Fairlife Ultra-Filtered", category = "Dairy & Eggs", servingSize = 240.0, servingUnit = "ml (1 cup)", calories = 80.0, protein = 13.0, carbs = 6.0, fats = 0.0, fiber = 0.0),
        FoodItem(name = "Cheddar Cheese (Shredded)", brand = "Kraft", category = "Dairy & Eggs", servingSize = 28.0, servingUnit = "g (1 oz)", calories = 110.0, protein = 7.0, carbs = 1.0, fats = 9.0, fiber = 0.0),

        // Carbs & Grains
        FoodItem(name = "Rolled Oats (Dry)", brand = "Quaker", category = "Carbs & Grains", servingSize = 50.0, servingUnit = "g (1/2 cup)", calories = 190.0, protein = 6.5, carbs = 33.0, fats = 3.0, fiber = 4.0),
        FoodItem(name = "Jasmine Rice (Cooked)", brand = "Mahatma", category = "Carbs & Grains", servingSize = 150.0, servingUnit = "g (1 cup)", calories = 195.0, protein = 4.0, carbs = 43.0, fats = 0.4, fiber = 0.6),
        FoodItem(name = "Brown Rice (Cooked)", brand = "Lundberg", category = "Carbs & Grains", servingSize = 150.0, servingUnit = "g (1 cup)", calories = 165.0, protein = 3.8, carbs = 35.0, fats = 1.4, fiber = 2.5),
        FoodItem(name = "Sweet Potato (Baked)", brand = "Produce", category = "Carbs & Grains", servingSize = 150.0, servingUnit = "g (1 medium)", calories = 135.0, protein = 3.0, carbs = 31.0, fats = 0.2, fiber = 4.5),
        FoodItem(name = "Whole Wheat Bread", brand = "Ezekiel 4:9", category = "Carbs & Grains", servingSize = 1.0, servingUnit = "slice (34g)", calories = 80.0, protein = 5.0, carbs = 15.0, fats = 0.5, fiber = 3.0),
        FoodItem(name = "Whole Wheat Pasta (Cooked)", brand = "Barilla", category = "Carbs & Grains", servingSize = 140.0, servingUnit = "g (1 cup)", calories = 174.0, protein = 7.5, carbs = 37.0, fats = 0.8, fiber = 4.5),
        FoodItem(name = "Quinoa (Cooked)", brand = "Ancient Harvest", category = "Carbs & Grains", servingSize = 150.0, servingUnit = "g (1 cup)", calories = 180.0, protein = 6.0, carbs = 32.0, fats = 2.5, fiber = 3.8),
        FoodItem(name = "Bagel (Plain, Medium)", brand = "Thomas", category = "Carbs & Grains", servingSize = 1.0, servingUnit = "piece (95g)", calories = 260.0, protein = 9.0, carbs = 53.0, fats = 1.5, fiber = 2.0),
        FoodItem(name = "Chickpeas (Canned, Drained)", brand = "Goya", category = "Carbs & Grains", servingSize = 100.0, servingUnit = "g", calories = 139.0, protein = 7.0, carbs = 22.0, fats = 2.5, fiber = 6.0),
        FoodItem(name = "Black Beans (Cooked)", brand = "Bush's", category = "Carbs & Grains", servingSize = 100.0, servingUnit = "g", calories = 132.0, protein = 8.9, carbs = 23.7, fats = 0.5, fiber = 8.7),

        // Fruits & Veggies
        FoodItem(name = "Banana (Medium)", brand = "Fresh Produce", category = "Fruits & Veggies", servingSize = 1.0, servingUnit = "piece (118g)", calories = 105.0, protein = 1.3, carbs = 27.0, fats = 0.3, fiber = 3.1),
        FoodItem(name = "Apple (Gala/Fuji)", brand = "Fresh Produce", category = "Fruits & Veggies", servingSize = 1.0, servingUnit = "piece (182g)", calories = 95.0, protein = 0.5, carbs = 25.0, fats = 0.3, fiber = 4.4),
        FoodItem(name = "Blueberries (Fresh)", brand = "Driscoll's", category = "Fruits & Veggies", servingSize = 100.0, servingUnit = "g", calories = 57.0, protein = 0.7, carbs = 14.5, fats = 0.3, fiber = 2.4),
        FoodItem(name = "Broccoli (Steamed)", brand = "Fresh Produce", category = "Fruits & Veggies", servingSize = 100.0, servingUnit = "g", calories = 35.0, protein = 2.4, carbs = 7.0, fats = 0.4, fiber = 3.3),
        FoodItem(name = "Baby Spinach (Raw)", brand = "Organic Earthbound", category = "Fruits & Veggies", servingSize = 50.0, servingUnit = "g (2 cups)", calories = 12.0, protein = 1.5, carbs = 1.8, fats = 0.2, fiber = 1.1),
        FoodItem(name = "Avocado (Hass)", brand = "Fresh Produce", category = "Fruits & Veggies", servingSize = 50.0, servingUnit = "g (1/3 fruit)", calories = 80.0, protein = 1.0, carbs = 4.0, fats = 7.3, fiber = 3.4),
        FoodItem(name = "Strawberries (Fresh)", brand = "Driscoll's", category = "Fruits & Veggies", servingSize = 100.0, servingUnit = "g", calories = 32.0, protein = 0.7, carbs = 7.7, fats = 0.3, fiber = 2.0),
        FoodItem(name = "Asparagus (Grilled)", brand = "Fresh Produce", category = "Fruits & Veggies", servingSize = 100.0, servingUnit = "g", calories = 22.0, protein = 2.4, carbs = 4.1, fats = 0.2, fiber = 2.0),

        // Healthy Fats & Nuts
        FoodItem(name = "Natural Peanut Butter", brand = "Jif Natural / Smucker's", category = "Fats", servingSize = 32.0, servingUnit = "g (2 tbsp)", calories = 190.0, protein = 8.0, carbs = 7.0, fats = 16.0, fiber = 2.0),
        FoodItem(name = "Almonds (Raw)", brand = "Blue Diamond", category = "Fats", servingSize = 28.0, servingUnit = "g (1 oz / ~23 nuts)", calories = 164.0, protein = 6.0, carbs = 6.1, fats = 14.2, fiber = 3.5),
        FoodItem(name = "Extra Virgin Olive Oil", brand = "California Olive Ranch", category = "Fats", servingSize = 14.0, servingUnit = "ml (1 tbsp)", calories = 120.0, protein = 0.0, carbs = 0.0, fats = 14.0, fiber = 0.0),
        FoodItem(name = "Chia Seeds", brand = "Nutiva", category = "Fats", servingSize = 15.0, servingUnit = "g (1 tbsp)", calories = 73.0, protein = 2.5, carbs = 6.3, fats = 4.6, fiber = 5.1),
        FoodItem(name = "Walnuts", brand = "Kirkland", category = "Fats", servingSize = 28.0, servingUnit = "g (1 oz)", calories = 185.0, protein = 4.3, carbs = 3.9, fats = 18.5, fiber = 1.9),

        // Snacks & Recipes
        FoodItem(name = "High Protein Granola Bar", brand = "Barebells / Quest", category = "Snacks", servingSize = 1.0, servingUnit = "bar (60g)", calories = 200.0, protein = 20.0, carbs = 16.0, fats = 7.0, fiber = 3.0),
        FoodItem(name = "Rice Cakes (Lightly Salted)", brand = "Lundberg", category = "Snacks", servingSize = 2.0, servingUnit = "cakes (28g)", calories = 100.0, protein = 2.0, carbs = 22.0, fats = 0.5, fiber = 1.0),
        FoodItem(name = "Overnight Oats with Berries & Whey", brand = "Chef Fit", category = "Meals & Recipes", servingSize = 1.0, servingUnit = "bowl (320g)", calories = 420.0, protein = 35.0, carbs = 52.0, fats = 8.0, fiber = 7.0, isCustomRecipe = true, recipeIngredients = "Rolled Oats 50g, Whey Protein 32g, Almond Milk 200ml, Chia Seeds 10g, Blueberries 50g"),
        FoodItem(name = "Grilled Chicken & Quinoa Power Bowl", brand = "Fit Kitchen", category = "Meals & Recipes", servingSize = 1.0, servingUnit = "bowl (400g)", calories = 540.0, protein = 48.0, carbs = 56.0, fats = 14.0, fiber = 8.0, isCustomRecipe = true, recipeIngredients = "Chicken Breast 150g, Quinoa 150g, Avocado 50g, Spinach 50g, Olive Oil 5ml")
    )

    val starterExercises = listOf(
        // Chest
        Exercise(
            id = "bench_press",
            name = "Barbell Bench Press",
            category = "Chest",
            equipment = "Barbell & Bench",
            primaryMuscle = "Pectoralis Major",
            secondaryMuscles = "Triceps, Anterior Deltoids",
            instructions = "Lie flat on the bench, grip the barbell slightly wider than shoulder width. Unrack and lower bar smoothly to mid-chest, keeping elbows at 45 degrees. Press upward explosively while driving feet into floor.",
            formTips = "Keep shoulder blades retracted and pinched into the bench throughout the lift.",
            animationType = "bench_press",
            isTimeBased = false
        ),
        Exercise(
            id = "incline_dumbbell_press",
            name = "Incline Dumbbell Press",
            category = "Chest",
            equipment = "Dumbbells & Incline Bench",
            primaryMuscle = "Upper Chest (Clavicular Head)",
            secondaryMuscles = "Triceps, Front Delts",
            instructions = "Set bench to 30-45 degrees. Press dumbbells upward over chest, rotating slightly at the top. Lower with controlled tempo until dumbbells reach chest level.",
            formTips = "Do not set incline too steep to avoid excessive shoulder activation.",
            animationType = "bench_press",
            isTimeBased = false
        ),
        Exercise(
            id = "pushups",
            name = "Standard Push-Ups",
            category = "Chest",
            equipment = "Bodyweight",
            primaryMuscle = "Chest & Core",
            secondaryMuscles = "Triceps, Shoulders",
            instructions = "Place hands shoulder-width apart. Maintain a strict plank line from head to heels. Lower chest until 1 inch off the floor, then push back up forcefully.",
            formTips = "Engage glutes and core to prevent hips from sagging.",
            animationType = "pushup",
            isTimeBased = false
        ),
        Exercise(
            id = "cable_chest_fly",
            name = "Cable Chest Fly",
            category = "Chest",
            equipment = "Cable Machine",
            primaryMuscle = "Pectorals (Inner/Squeeze)",
            secondaryMuscles = "Front Delts",
            instructions = "Set pulleys at chest height. Step forward, keep slight bend in elbows. Bring handles together in a hugging motion, squeezing chest at peak contraction.",
            formTips = "Focus on opening the chest on the eccentric stretch.",
            animationType = "lateral_raise",
            isTimeBased = false
        ),

        // Back
        Exercise(
            id = "barbell_deadlift",
            name = "Conventional Deadlift",
            category = "Back",
            equipment = "Barbell & Plates",
            primaryMuscle = "Erector Spinae & Posterior Chain",
            secondaryMuscles = "Glutes, Hamstrings, Lats, Traps",
            instructions = "Stand with feet hip-width apart, barbell over midfoot. Hinge at hips to grip bar. Brace core, engage lats, and drive through the floor to stand tall.",
            formTips = "Keep bar dragging close to shins and maintain a neutral spine. Do not round lower back.",
            animationType = "deadlift",
            isTimeBased = false
        ),
        Exercise(
            id = "lat_pulldown",
            name = "Wide-Grip Lat Pulldown",
            category = "Back",
            equipment = "Cable Machine / Lat Pulldown",
            primaryMuscle = "Latissimus Dorsi",
            secondaryMuscles = "Biceps, Rear Deltoids, Rhomboids",
            instructions = "Sit at machine with thighs snug under pads. Grip bar wide with overhand grip. Pull bar down toward upper chest by driving elbows down and back.",
            formTips = "Squeeze shoulder blades together at the bottom; avoid excessive backward swinging.",
            animationType = "lat_pulldown",
            isTimeBased = false
        ),
        Exercise(
            id = "barbell_bent_row",
            name = "Barbell Bent-Over Row",
            category = "Back",
            equipment = "Barbell",
            primaryMuscle = "Mid-Back & Rhomboids",
            secondaryMuscles = "Lats, Biceps, Core",
            instructions = "Hinge forward at 45 degrees with knees slightly bent. Pull bar up toward lower ribcage / navel, driving elbows up toward the ceiling.",
            formTips = "Keep neck neutral and torso braced throughout the set.",
            animationType = "deadlift",
            isTimeBased = false
        ),
        Exercise(
            id = "pullups",
            name = "Pull-Ups (Overhand)",
            category = "Back",
            equipment = "Pull-Up Bar",
            primaryMuscle = "Lats & Upper Back",
            secondaryMuscles = "Biceps, Core",
            instructions = "Hang from bar with overhand grip wider than shoulders. Pull chest up to bar level by driving elbows downward.",
            formTips = "Control the lowering phase completely; avoid kipping.",
            animationType = "lat_pulldown",
            isTimeBased = false
        ),

        // Legs
        Exercise(
            id = "barbell_back_squat",
            name = "Barbell Back Squat",
            category = "Legs",
            equipment = "Barbell & Squat Rack",
            primaryMuscle = "Quadriceps & Glutes",
            secondaryMuscles = "Hamstrings, Core, Calves",
            instructions = "Rest barbell across upper traps. Stand shoulder-width, toes angled slightly out. Descend by bending knees and hips until thighs are parallel to ground. Drive up explosively.",
            formTips = "Keep knees tracking in line with toes and chest elevated.",
            animationType = "squat",
            isTimeBased = false
        ),
        Exercise(
            id = "romanian_deadlift",
            name = "Romanian Deadlift (RDL)",
            category = "Legs",
            equipment = "Barbell or Dumbbells",
            primaryMuscle = "Hamstrings & Glutes",
            secondaryMuscles = "Lower Back, Forearms",
            instructions = "Hold bar at hips with soft bend in knees. Push hips backward while lowering weight along thighs until deep hamstring stretch is felt. Drive hips forward to lockout.",
            formTips = "Think of closing a car door with your glutes.",
            animationType = "deadlift",
            isTimeBased = false
        ),
        Exercise(
            id = "walking_lunges",
            name = "Dumbbell Walking Lunges",
            category = "Legs",
            equipment = "Dumbbells",
            primaryMuscle = "Quadriceps & Glute Medius",
            secondaryMuscles = "Hamstrings, Calves",
            instructions = "Hold dumbbells at sides. Step forward into lunge until both knees reach 90-degree angles. Step through with rear leg into next lunge.",
            formTips = "Keep front knee stable and torso upright.",
            animationType = "lunge",
            isTimeBased = false
        ),
        Exercise(
            id = "leg_press",
            name = "Seated Leg Press",
            category = "Legs",
            equipment = "Leg Press Machine",
            primaryMuscle = "Quadriceps & Glutes",
            secondaryMuscles = "Hamstrings",
            instructions = "Place feet hip-width on footplate. Release safety stops and lower sled until knees form 90 degrees. Press platform back without locking knees fully.",
            formTips = "Keep lower back and glutes pinned to the seat padding.",
            animationType = "squat",
            isTimeBased = false
        ),

        // Shoulders
        Exercise(
            id = "overhead_press",
            name = "Overhead Barbell Military Press",
            category = "Shoulders",
            equipment = "Barbell",
            primaryMuscle = "Anterior & Lateral Deltoids",
            secondaryMuscles = "Triceps, Upper Chest, Core",
            instructions = "Hold barbell at collarbone height. Press straight overhead, locking out with bar aligned directly over shoulders and midfoot. Lower smoothly.",
            formTips = "Squeeze glutes and brace core to prevent hyperextending lower back.",
            animationType = "overhead_press",
            isTimeBased = false
        ),
        Exercise(
            id = "dumbbell_lateral_raise",
            name = "Dumbbell Lateral Raise",
            category = "Shoulders",
            equipment = "Dumbbells",
            primaryMuscle = "Lateral (Side) Deltoids",
            secondaryMuscles = "Traps",
            instructions = "Hold dumbbells at sides. Raise arms outward with slight elbow bend until parallel with shoulders. Lower slowly with resistance.",
            formTips = "Lead with the elbows and pour imaginary water pitchers at the top.",
            animationType = "lateral_raise",
            isTimeBased = false
        ),

        // Arms
        Exercise(
            id = "dumbbell_bicep_curl",
            name = "Standing Dumbbell Bicep Curl",
            category = "Arms",
            equipment = "Dumbbells",
            primaryMuscle = "Biceps Brachii",
            secondaryMuscles = "Brachialis, Forearms",
            instructions = "Stand tall with dumbbells at sides. Curl weights upward while supinating wrists (palms facing up). Squeeze biceps at the top and lower under full control.",
            formTips = "Keep elbows pinned at sides; do not swing weights.",
            animationType = "bicep_curl",
            isTimeBased = false
        ),
        Exercise(
            id = "tricep_dips",
            name = "Parallel Bar Tricep Dips",
            category = "Arms",
            equipment = "Dip Station",
            primaryMuscle = "Triceps Brachii",
            secondaryMuscles = "Chest, Shoulders",
            instructions = "Support body on dip bars with arms locked. Lower torso by bending elbows until 90 degrees, keeping body upright. Push back up to lockout.",
            formTips = "Keep elbows tucked to maximize tricep isolation.",
            animationType = "tricep_dips",
            isTimeBased = false
        ),

        // Core & Cardio
        Exercise(
            id = "plank_hold",
            name = "Isometric Plank Hold",
            category = "Core",
            equipment = "Bodyweight / Mat",
            primaryMuscle = "Rectus Abdominis & Transverse Abdominis",
            secondaryMuscles = "Shoulders, Glutes",
            instructions = "Rest on forearms and toes. Keep spine neutral and body rigid in straight line. Hold position while breathing steadily.",
            formTips = "Actively pull belly button to spine and contract glutes.",
            animationType = "plank",
            isTimeBased = true
        ),
        Exercise(
            id = "treadmill_interval_run",
            name = "Treadmill High Intensity Run",
            category = "Cardio",
            equipment = "Treadmill",
            primaryMuscle = "Cardiovascular & Legs",
            secondaryMuscles = "Calves, Hamstrings, Core",
            instructions = "Warm up for 2 minutes, then alternate 1 minute sprint with 1 minute recovery jog. Maintain upright posture and rhythmic arm drive.",
            formTips = "Land on midfoot and maintain consistent breathing cadence.",
            animationType = "running",
            isTimeBased = true
        )
    )

    fun createInitialRoutines(): List<WorkoutRoutine> {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, RoutineExercise::class.java)
        val adapter = moshi.adapter<List<RoutineExercise>>(type)

        val pushDayExercises = listOf(
            RoutineExercise("bench_press", "Barbell Bench Press", targetSets = 4, targetReps = 8, weightKg = 70.0, restTimeSeconds = 90, notes = "Heavy working sets"),
            RoutineExercise("incline_dumbbell_press", "Incline Dumbbell Press", targetSets = 3, targetReps = 10, weightKg = 24.0, restTimeSeconds = 75, notes = "Full upper stretch"),
            RoutineExercise("overhead_press", "Overhead Barbell Military Press", targetSets = 3, targetReps = 10, weightKg = 45.0, restTimeSeconds = 75, notes = "Lockout overhead"),
            RoutineExercise("dumbbell_lateral_raise", "Dumbbell Lateral Raise", targetSets = 4, targetReps = 12, weightKg = 12.0, restTimeSeconds = 60, notes = "Strict form, slow eccentric"),
            RoutineExercise("tricep_dips", "Parallel Bar Tricep Dips", targetSets = 3, targetReps = 12, weightKg = 0.0, restTimeSeconds = 60, notes = "Bodyweight burn")
        )

        val pullDayExercises = listOf(
            RoutineExercise("barbell_deadlift", "Conventional Deadlift", targetSets = 3, targetReps = 5, weightKg = 110.0, restTimeSeconds = 120, notes = "Brace core tight"),
            RoutineExercise("lat_pulldown", "Wide-Grip Lat Pulldown", targetSets = 4, targetReps = 10, weightKg = 60.0, restTimeSeconds = 75, notes = "Squeeze lats"),
            RoutineExercise("barbell_bent_row", "Barbell Bent-Over Row", targetSets = 3, targetReps = 10, weightKg = 60.0, restTimeSeconds = 75, notes = "Drive elbows high"),
            RoutineExercise("dumbbell_bicep_curl", "Standing Dumbbell Bicep Curl", targetSets = 4, targetReps = 12, weightKg = 14.0, restTimeSeconds = 60, notes = "Supinate at peak")
        )

        val legDayExercises = listOf(
            RoutineExercise("barbell_back_squat", "Barbell Back Squat", targetSets = 4, targetReps = 8, weightKg = 90.0, restTimeSeconds = 120, notes = "Parallel depth"),
            RoutineExercise("romanian_deadlift", "Romanian Deadlift (RDL)", targetSets = 3, targetReps = 10, weightKg = 75.0, restTimeSeconds = 90, notes = "Feel hamstring stretch"),
            RoutineExercise("walking_lunges", "Dumbbell Walking Lunges", targetSets = 3, targetReps = 12, weightKg = 16.0, restTimeSeconds = 75, notes = "Each leg"),
            RoutineExercise("plank_hold", "Isometric Plank Hold", targetSets = 3, targetReps = 0, targetDurationSeconds = 60, weightKg = 0.0, restTimeSeconds = 45, notes = "Solid core hold")
        )

        val fullBodyExercises = listOf(
            RoutineExercise("pushups", "Standard Push-Ups", targetSets = 3, targetReps = 15, weightKg = 0.0, restTimeSeconds = 45, notes = "Warmup volume"),
            RoutineExercise("barbell_back_squat", "Barbell Back Squat", targetSets = 3, targetReps = 10, weightKg = 80.0, restTimeSeconds = 90, notes = "Core compound"),
            RoutineExercise("lat_pulldown", "Wide-Grip Lat Pulldown", targetSets = 3, targetReps = 12, weightKg = 55.0, restTimeSeconds = 60, notes = "Back builder"),
            RoutineExercise("treadmill_interval_run", "Treadmill High Intensity Run", targetSets = 1, targetReps = 0, targetDurationSeconds = 600, weightKg = 0.0, restTimeSeconds = 60, notes = "10 min HIIT intervals")
        )

        return listOf(
            WorkoutRoutine(id = 1, name = "Push Day: Hypertrophy & Power", description = "Chest, Shoulders, and Triceps focused muscle builder", dayOfWeek = "Monday", targetDurationMinutes = 50, exercisesJson = adapter.toJson(pushDayExercises)),
            WorkoutRoutine(id = 2, name = "Pull Day: Back & Biceps Strength", description = "Deadlifts, Lat Pulldowns, Rows, and Arm sculpting", dayOfWeek = "Wednesday", targetDurationMinutes = 55, exercisesJson = adapter.toJson(pullDayExercises)),
            WorkoutRoutine(id = 3, name = "Legs & Core Destruction", description = "Heavy squats, posterior chain hamstring RDLs, lunges, and plank", dayOfWeek = "Friday", targetDurationMinutes = 50, exercisesJson = adapter.toJson(legDayExercises)),
            WorkoutRoutine(id = 4, name = "Full Body Athletic Conditioning", description = "Dynamic circuit combining compound strength and cardio sprint intervals", dayOfWeek = "Saturday", targetDurationMinutes = 40, exercisesJson = adapter.toJson(fullBodyExercises))
        )
    }

    val starterWeeklyMealPlan = listOf(
        // Monday
        WeeklyMealPlanItem(dayOfWeek = "Monday", mealType = "Breakfast", title = "Power Berry Protein Oatmeal", description = "Warm rolled oats cooked in almond milk, folded with whey protein, chia seeds, and topped with wild blueberries.", ingredients = "Rolled Oats 50g, Whey Protein 30g, Chia Seeds 10g, Blueberries 60g, Almond Milk 200ml", calories = 430, protein = 34.0, carbs = 52.0, fats = 9.0),
        WeeklyMealPlanItem(dayOfWeek = "Monday", mealType = "Lunch", title = "Grilled Herb Chicken & Quinoa Bowl", description = "Tender grilled chicken breast over fluffy seasoned quinoa, roasted broccoli, and sliced Hass avocado.", ingredients = "Chicken Breast 160g, Cooked Quinoa 150g, Broccoli 100g, Avocado 40g, Olive Oil 5ml", calories = 560, protein = 52.0, carbs = 48.0, fats = 16.0),
        WeeklyMealPlanItem(dayOfWeek = "Monday", mealType = "Dinner", title = "Pan-Seared Salmon with Sweet Potato", description = "Omega-3 rich wild salmon fillet served alongside baked sweet potato wedges and grilled asparagus.", ingredients = "Salmon Fillet 160g, Baked Sweet Potato 180g, Asparagus 100g, Garlic & Herbs", calories = 580, protein = 42.0, carbs = 42.0, fats = 22.0),
        WeeklyMealPlanItem(dayOfWeek = "Monday", mealType = "Snack", title = "Greek Yogurt with Honey & Almonds", description = "Creamy 0% Greek yogurt swirled with crunchy raw almonds and a touch of organic raw honey.", ingredients = "Greek Yogurt 180g, Raw Almonds 20g, Honey 10g", calories = 230, protein = 21.0, carbs = 16.0, fats = 8.0),

        // Tuesday
        WeeklyMealPlanItem(dayOfWeek = "Tuesday", mealType = "Breakfast", title = "Scrambled Eggs & Avocado Toast", description = "3 scrambled farm eggs served on toasted whole grain Ezekiel bread with mashed avocado and sea salt.", ingredients = "Eggs 3 large, Whole Wheat Bread 2 slices, Avocado 50g", calories = 480, protein = 26.0, carbs = 34.0, fats = 24.0),
        WeeklyMealPlanItem(dayOfWeek = "Tuesday", mealType = "Lunch", title = "Lean Turkey & Brown Rice Stir-Fry", description = "Ground lean turkey breast sautéed with bell peppers, snap peas, low-sodium tamari, over brown rice.", ingredients = "Ground Turkey 170g, Brown Rice 150g, Mixed Veggies 120g, Sesame Oil 5ml", calories = 540, protein = 46.0, carbs = 52.0, fats = 14.0),
        WeeklyMealPlanItem(dayOfWeek = "Tuesday", mealType = "Dinner", title = "Sirloin Steak with Roasted Red Potatoes", description = "Lean top sirloin grilled to medium-rare paired with rosemary roasted baby potatoes and garden salad.", ingredients = "Top Sirloin 170g, Red Potatoes 200g, Mixed Greens 80g, Olive Oil 8ml", calories = 610, protein = 49.0, carbs = 44.0, fats = 19.0),
        WeeklyMealPlanItem(dayOfWeek = "Tuesday", mealType = "Snack", title = "Peanut Butter Rice Cakes & Banana", description = "Crunchy brown rice cakes topped with natural creamy peanut butter and banana coins.", ingredients = "Rice Cakes 2, Peanut Butter 25g, Banana 1/2", calories = 260, protein = 8.0, carbs = 32.0, fats = 12.0),

        // Wednesday
        WeeklyMealPlanItem(dayOfWeek = "Wednesday", mealType = "Breakfast", title = "Triple Berry Whey Smoothie Bowl", description = "Blended frozen strawberries, banana, spinach, vanilla whey isolate topped with chia and granola.", ingredients = "Whey 32g, Frozen Berries 120g, Banana 1, Spinach 40g, Granola 25g", calories = 440, protein = 35.0, carbs = 58.0, fats = 7.0),
        WeeklyMealPlanItem(dayOfWeek = "Wednesday", mealType = "Lunch", title = "Mediterranean Tuna & Chickpea Salad", description = "Solid white albacore tuna tossed with crisp chickpeas, cucumber, cherry tomatoes, and lemon olive oil.", ingredients = "Canned Tuna 140g, Chickpeas 120g, Cucumber 60g, Tomatoes 60g, Olive Oil 10ml", calories = 490, protein = 44.0, carbs = 38.0, fats = 15.0),
        WeeklyMealPlanItem(dayOfWeek = "Wednesday", mealType = "Dinner", title = "Lemon Garlic Chicken Pasta", description = "High-protein whole wheat penne with grilled sliced chicken breast in light garlic herb sauce.", ingredients = "Chicken Breast 160g, Whole Wheat Pasta 100g (dry), Garlic 2 cloves, Olive Oil 8ml", calories = 620, protein = 54.0, carbs = 68.0, fats = 12.0),
        WeeklyMealPlanItem(dayOfWeek = "Wednesday", mealType = "Snack", title = "Low-Fat Cottage Cheese & Apple", description = "Whipped probiotic cottage cheese served with crisp Fuji apple slices and cinnamon.", ingredients = "Cottage Cheese 150g, Apple 1 medium, Cinnamon", calories = 210, protein = 18.0, carbs = 28.0, fats = 3.0),

        // Thursday
        WeeklyMealPlanItem(dayOfWeek = "Thursday", mealType = "Breakfast", title = "Egg White & Spinach Breakfast Burrito", description = "Fluffy scrambled egg whites with baby spinach, turkey bacon, and salsa wrapped in a whole grain tortilla.", ingredients = "Egg Whites 150g, Turkey Bacon 2 slices, Spinach 50g, Whole Wheat Tortilla 1", calories = 390, protein = 36.0, carbs = 32.0, fats = 9.0),
        WeeklyMealPlanItem(dayOfWeek = "Thursday", mealType = "Lunch", title = "Grilled Chicken Caesar Macro Wrap", description = "Sliced chicken breast with crisp romaine, parmesan, and light Greek yogurt caesar dressing.", ingredients = "Chicken Breast 160g, Romaine Lettuce 80g, Parmesan 15g, Whole Wheat Wrap 1", calories = 510, protein = 48.0, carbs = 36.0, fats = 15.0),
        WeeklyMealPlanItem(dayOfWeek = "Thursday", mealType = "Dinner", title = "Asian Glazed Tofu & Quinoa Bowl", description = "Crispy baked firm tofu in ginger-soy glaze over steamed quinoa and sesame sautéed green beans.", ingredients = "Firm Tofu 200g, Quinoa 150g, Green Beans 120g, Soy-Ginger Glaze 20ml", calories = 530, protein = 32.0, carbs = 58.0, fats = 18.0),
        WeeklyMealPlanItem(dayOfWeek = "Thursday", mealType = "Snack", title = "Protein Bar & Fresh Blueberries", description = "Low-sugar high protein wafer bar paired with antioxidant-rich fresh blueberries.", ingredients = "Protein Bar 60g, Fresh Blueberries 80g", calories = 250, protein = 21.0, carbs = 26.0, fats = 8.0),

        // Friday
        WeeklyMealPlanItem(dayOfWeek = "Friday", mealType = "Breakfast", title = "Protein Banana Pancakes", description = "3-ingredient blender pancakes made of rolled oats, banana, and whey protein, cooked golden.", ingredients = "Rolled Oats 45g, Banana 1, Whey Protein 25g, Egg 1", calories = 430, protein = 32.0, carbs = 54.0, fats = 8.0),
        WeeklyMealPlanItem(dayOfWeek = "Friday", mealType = "Lunch", title = "Beef Burrito Bowl with Black Beans", description = "Lean ground beef over jasmine rice, black beans, sweet corn, salsa verde, and fresh cilantro.", ingredients = "Lean Beef 150g, Jasmine Rice 120g, Black Beans 80g, Salsa 40g", calories = 580, protein = 45.0, carbs = 62.0, fats = 15.0),
        WeeklyMealPlanItem(dayOfWeek = "Friday", mealType = "Dinner", title = "Herb Roasted Salmon with Quinoa Pilaf", description = "Atlantic salmon fillet with lemon dill seasoning over warm quinoa and roasted Brussels sprouts.", ingredients = "Salmon 160g, Quinoa 140g, Brussels Sprouts 120g, Olive Oil 5ml", calories = 590, protein = 43.0, carbs = 42.0, fats = 23.0),
        WeeklyMealPlanItem(dayOfWeek = "Friday", mealType = "Snack", title = "Mixed Raw Nuts & Dark Chocolate", description = "Portion-controlled almonds, walnuts, and 85% cocoa dark chocolate square.", ingredients = "Almonds 15g, Walnuts 15g, Dark Chocolate 15g", calories = 260, protein = 6.0, carbs = 14.0, fats = 20.0),

        // Saturday
        WeeklyMealPlanItem(dayOfWeek = "Saturday", mealType = "Breakfast", title = "Ultimate Weekend Omelette", description = "3 whole eggs with baby spinach, button mushrooms, diced turkey breast, and low-fat cheddar.", ingredients = "Eggs 3, Turkey Breast 50g, Spinach 40g, Mushrooms 40g, Cheddar 20g", calories = 460, protein = 38.0, carbs = 6.0, fats = 26.0),
        WeeklyMealPlanItem(dayOfWeek = "Saturday", mealType = "Lunch", title = "Grilled Chicken Clubhouse Bowl", description = "Chopped chicken breast, crispy bacon crumble, hard-boiled egg, avocado, and mixed field greens.", ingredients = "Chicken Breast 150g, Turkey Bacon 2 slices, Egg 1, Avocado 40g, Greens 100g", calories = 520, protein = 52.0, carbs = 12.0, fats = 24.0),
        WeeklyMealPlanItem(dayOfWeek = "Saturday", mealType = "Dinner", title = "Homemade High-Protein Pita Pizza", description = "Crispy whole wheat pita topped with crushed tomato sauce, low-fat mozzarella, shredded chicken, and basil.", ingredients = "Pita 1 large, Tomato Sauce 60g, Mozzarella 60g, Shredded Chicken 120g", calories = 570, protein = 48.0, carbs = 46.0, fats = 16.0),
        WeeklyMealPlanItem(dayOfWeek = "Saturday", mealType = "Snack", title = "Greek Yogurt Berry Parfait", description = "Layered Greek yogurt with crushed walnuts and fresh ripe strawberries.", ingredients = "Greek Yogurt 170g, Strawberries 100g, Walnuts 15g", calories = 240, protein = 20.0, carbs = 18.0, fats = 9.0),

        // Sunday
        WeeklyMealPlanItem(dayOfWeek = "Sunday", mealType = "Breakfast", title = "Loaded Cinnamon Oats with Peanut Butter", description = "Hearty warm oats topped with creamy natural peanut butter, chia seeds, and sliced banana.", ingredients = "Oats 50g, Peanut Butter 20g, Chia 10g, Banana 1/2, Cinnamon", calories = 450, protein = 16.0, carbs = 60.0, fats = 17.0),
        WeeklyMealPlanItem(dayOfWeek = "Sunday", mealType = "Lunch", title = "Grilled Steak & Sweet Potato Mash", description = "Tender grilled top sirloin steak served with roasted sweet potato mash and steamed green beans.", ingredients = "Sirloin 160g, Sweet Potato 200g, Green Beans 100g, Olive Oil 5ml", calories = 580, protein = 46.0, carbs = 48.0, fats = 16.0),
        WeeklyMealPlanItem(dayOfWeek = "Sunday", mealType = "Dinner", title = "Shrimp & Veggie Brown Rice Skillet", description = "Succulent shrimp sautéed with bell peppers, zucchini, garlic, and cooked brown rice.", ingredients = "Shrimp 180g, Brown Rice 150g, Mixed Veggies 120g, Olive Oil 8ml", calories = 520, protein = 42.0, carbs = 54.0, fats = 11.0),
        WeeklyMealPlanItem(dayOfWeek = "Sunday", mealType = "Snack", title = "Whey Protein Shake & Rice Cake", description = "Rapid absorption post-workout whey shake with 1 crispy lightly salted rice cake.", ingredients = "Whey 30g, Water 300ml, Rice Cake 1", calories = 170, protein = 25.0, carbs = 13.0, fats = 1.5)
    )

    fun generatePastMonthWeightLogs(baseDate: Date = Date()): List<WeightLog> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        val logs = mutableListOf<WeightLog>()

        // 30-day progression checkpoints from 78.6 kg down to 76.5 kg today
        val checkpoints = listOf(
            30 to (78.6 to "Starting FitPulse macro & training cycle"),
            27 to (78.4 to "Morning weigh-in after fasted cardio"),
            24 to (78.2 to "Week 1 check-in, energy levels high"),
            21 to (77.9 to "Hit all protein & hydration goals"),
            18 to (77.7 to "Waist down 1cm, clean eating holding"),
            15 to (77.4 to "Mid-month milestone, strength steady"),
            12 to (77.2 to "Post leg-day weigh-in, recovered well"),
            9 to (77.0 to "Sub-77kg barrier achieved!"),
            6 to (76.8 to "Progressive overload on bench & squats"),
            3 to (76.6 to "Solid weekend macro tracking"),
            0 to (76.5 to "Morning weigh-in, feeling lean & strong")
        )

        for ((daysAgo, data) in checkpoints) {
            cal.time = baseDate
            cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
            val dateStr = sdf.format(cal.time)
            logs.add(
                WeightLog(
                    date = dateStr,
                    weightKg = data.first,
                    notes = data.second,
                    timestamp = cal.timeInMillis
                )
            )
        }
        return logs
    }

    val starterWeightLogs: List<WeightLog>
        get() = generatePastMonthWeightLogs()

    fun generatePastMonthMealLogs(baseDate: Date = Date()): List<MealLog> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        val logs = mutableListOf<MealLog>()

        // 7 rotational daily meal templates to give varied, realistic nutrition across the month
        val mealRotation = listOf(
            // Day Template 1: High Protein Oats + Chicken Quinoa + Salmon + Greek Yogurt
            listOf(
                MealLog(date = "", mealType = "BREAKFAST", foodName = "Power Berry Protein Oatmeal", quantity = 1.0, servingUnit = "bowl", calories = 430.0, protein = 34.0, carbs = 52.0, fats = 9.0),
                MealLog(date = "", mealType = "LUNCH", foodName = "Grilled Herb Chicken & Quinoa Bowl", quantity = 1.0, servingUnit = "bowl", calories = 560.0, protein = 52.0, carbs = 48.0, fats = 16.0),
                MealLog(date = "", mealType = "DINNER", foodName = "Pan-Seared Salmon with Sweet Potato", quantity = 1.0, servingUnit = "plate", calories = 580.0, protein = 42.0, carbs = 42.0, fats = 22.0),
                MealLog(date = "", mealType = "SNACKS", foodName = "Greek Yogurt with Honey & Almonds", quantity = 1.0, servingUnit = "cup", calories = 230.0, protein = 21.0, carbs = 16.0, fats = 8.0)
            ),
            // Day Template 2: Scrambled Eggs + Turkey Stir-Fry + Sirloin Steak + PB Rice Cakes
            listOf(
                MealLog(date = "", mealType = "BREAKFAST", foodName = "Scrambled Eggs & Avocado Toast", quantity = 1.0, servingUnit = "serving", calories = 480.0, protein = 26.0, carbs = 34.0, fats = 24.0),
                MealLog(date = "", mealType = "LUNCH", foodName = "Lean Turkey & Brown Rice Stir-Fry", quantity = 1.0, servingUnit = "bowl", calories = 540.0, protein = 46.0, carbs = 52.0, fats = 14.0),
                MealLog(date = "", mealType = "DINNER", foodName = "Sirloin Steak with Roasted Red Potatoes", quantity = 1.0, servingUnit = "plate", calories = 610.0, protein = 49.0, carbs = 44.0, fats = 19.0),
                MealLog(date = "", mealType = "SNACKS", foodName = "Peanut Butter Rice Cakes & Banana", quantity = 1.0, servingUnit = "serving", calories = 260.0, protein = 8.0, carbs = 32.0, fats = 12.0)
            ),
            // Day Template 3: Smoothie Bowl + Tuna Mediterranean Salad + Garlic Chicken Pasta + Cottage Cheese
            listOf(
                MealLog(date = "", mealType = "BREAKFAST", foodName = "Triple Berry Whey Smoothie Bowl", quantity = 1.0, servingUnit = "bowl", calories = 440.0, protein = 35.0, carbs = 58.0, fats = 7.0),
                MealLog(date = "", mealType = "LUNCH", foodName = "Mediterranean Tuna & Chickpea Salad", quantity = 1.0, servingUnit = "plate", calories = 490.0, protein = 44.0, carbs = 38.0, fats = 15.0),
                MealLog(date = "", mealType = "DINNER", foodName = "Lemon Garlic Chicken Pasta", quantity = 1.0, servingUnit = "bowl", calories = 620.0, protein = 54.0, carbs = 68.0, fats = 12.0),
                MealLog(date = "", mealType = "SNACKS", foodName = "Low-Fat Cottage Cheese & Apple", quantity = 1.0, servingUnit = "serving", calories = 210.0, protein = 18.0, carbs = 28.0, fats = 3.0)
            ),
            // Day Template 4: Egg White Wrap + Chicken Caesar Wrap + Baked Tofu Quinoa + Protein Bar
            listOf(
                MealLog(date = "", mealType = "BREAKFAST", foodName = "Egg White & Spinach Burrito", quantity = 1.0, servingUnit = "wrap", calories = 390.0, protein = 36.0, carbs = 32.0, fats = 9.0),
                MealLog(date = "", mealType = "LUNCH", foodName = "Grilled Chicken Caesar Macro Wrap", quantity = 1.0, servingUnit = "wrap", calories = 510.0, protein = 48.0, carbs = 36.0, fats = 15.0),
                MealLog(date = "", mealType = "DINNER", foodName = "Asian Glazed Tofu & Quinoa Bowl", quantity = 1.0, servingUnit = "bowl", calories = 530.0, protein = 32.0, carbs = 58.0, fats = 18.0),
                MealLog(date = "", mealType = "SNACKS", foodName = "Protein Bar & Fresh Blueberries", quantity = 1.0, servingUnit = "serving", calories = 250.0, protein = 21.0, carbs = 26.0, fats = 8.0)
            ),
            // Day Template 5: Banana Protein Pancakes + Beef Burrito Bowl + Salmon Pilaf + Mixed Nuts
            listOf(
                MealLog(date = "", mealType = "BREAKFAST", foodName = "Protein Banana Pancakes", quantity = 1.0, servingUnit = "stack", calories = 430.0, protein = 32.0, carbs = 54.0, fats = 8.0),
                MealLog(date = "", mealType = "LUNCH", foodName = "Beef Burrito Bowl with Black Beans", quantity = 1.0, servingUnit = "bowl", calories = 580.0, protein = 45.0, carbs = 62.0, fats = 15.0),
                MealLog(date = "", mealType = "DINNER", foodName = "Herb Roasted Salmon with Quinoa", quantity = 1.0, servingUnit = "plate", calories = 590.0, protein = 43.0, carbs = 42.0, fats = 23.0),
                MealLog(date = "", mealType = "SNACKS", foodName = "Mixed Raw Nuts & Dark Chocolate", quantity = 1.0, servingUnit = "portion", calories = 260.0, protein = 6.0, carbs = 14.0, fats = 20.0)
            ),
            // Day Template 6: Weekend Omelette + Chicken Clubhouse + Pita Pizza + Berry Parfait
            listOf(
                MealLog(date = "", mealType = "BREAKFAST", foodName = "Ultimate Weekend Omelette", quantity = 1.0, servingUnit = "serving", calories = 460.0, protein = 38.0, carbs = 6.0, fats = 26.0),
                MealLog(date = "", mealType = "LUNCH", foodName = "Grilled Chicken Clubhouse Bowl", quantity = 1.0, servingUnit = "bowl", calories = 520.0, protein = 52.0, carbs = 12.0, fats = 24.0),
                MealLog(date = "", mealType = "DINNER", foodName = "Homemade High-Protein Pita Pizza", quantity = 1.0, servingUnit = "pizza", calories = 570.0, protein = 48.0, carbs = 46.0, fats = 16.0),
                MealLog(date = "", mealType = "SNACKS", foodName = "Greek Yogurt Berry Parfait", quantity = 1.0, servingUnit = "parfait", calories = 240.0, protein = 20.0, carbs = 18.0, fats = 9.0)
            ),
            // Day Template 7: Cinnamon Oats + Steak Sweet Potato + Shrimp Rice + Whey Shake
            listOf(
                MealLog(date = "", mealType = "BREAKFAST", foodName = "Loaded Cinnamon Oats with Peanut Butter", quantity = 1.0, servingUnit = "bowl", calories = 450.0, protein = 16.0, carbs = 60.0, fats = 17.0),
                MealLog(date = "", mealType = "LUNCH", foodName = "Grilled Steak & Sweet Potato Mash", quantity = 1.0, servingUnit = "plate", calories = 580.0, protein = 46.0, carbs = 48.0, fats = 16.0),
                MealLog(date = "", mealType = "DINNER", foodName = "Shrimp & Veggie Brown Rice Skillet", quantity = 1.0, servingUnit = "skillet", calories = 520.0, protein = 42.0, carbs = 54.0, fats = 11.0),
                MealLog(date = "", mealType = "SNACKS", foodName = "Whey Protein Shake & Rice Cake", quantity = 1.0, servingUnit = "shake", calories = 170.0, protein = 25.0, carbs = 13.0, fats = 1.5)
            )
        )

        // Generate past 30 days of complete meals
        for (i in 30 downTo 1) {
            cal.time = baseDate
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val dateStr = sdf.format(cal.time)
            val dayIndex = i % mealRotation.size
            val template = mealRotation[dayIndex]

            template.forEachIndexed { mealIdx, meal ->
                val timeOffset = when (meal.mealType) {
                    "BREAKFAST" -> 8 * 3600 * 1000L
                    "LUNCH" -> 13 * 3600 * 1000L
                    "SNACKS" -> 16 * 3600 * 1000L
                    else -> 19 * 3600 * 1000L
                }
                logs.add(
                    meal.copy(
                        date = dateStr,
                        loggedAt = cal.timeInMillis + timeOffset
                    )
                )
            }
        }

        // Present Day (Day 0 / Today): Active partial day (Breakfast + Lunch + Afternoon Snack logged)
        cal.time = baseDate
        val todayStr = sdf.format(cal.time)
        logs.add(
            MealLog(
                date = todayStr,
                mealType = "BREAKFAST",
                foodName = "Power Berry Protein Oatmeal",
                quantity = 1.0,
                servingUnit = "bowl (320g)",
                calories = 430.0,
                protein = 34.0,
                carbs = 52.0,
                fats = 9.0,
                loggedAt = cal.timeInMillis - (4 * 3600 * 1000L)
            )
        )
        logs.add(
            MealLog(
                date = todayStr,
                mealType = "LUNCH",
                foodName = "Grilled Herb Chicken & Quinoa Bowl",
                quantity = 1.0,
                servingUnit = "bowl (400g)",
                calories = 560.0,
                protein = 52.0,
                carbs = 48.0,
                fats = 16.0,
                loggedAt = cal.timeInMillis - (1 * 3600 * 1000L)
            )
        )
        logs.add(
            MealLog(
                date = todayStr,
                mealType = "SNACKS",
                foodName = "Greek Yogurt with Honey & Almonds",
                quantity = 1.0,
                servingUnit = "cup (210g)",
                calories = 230.0,
                protein = 21.0,
                carbs = 16.0,
                fats = 8.0,
                loggedAt = cal.timeInMillis
            )
        )

        return logs
    }

    fun generatePastMonthWorkoutHistory(baseDate: Date = Date()): List<WorkoutHistory> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        val histories = mutableListOf<WorkoutHistory>()

        // 18 workout sessions spread across 30 days representing consistent 4-day split
        val sessionTemplates = listOf(
            30 to ("Push Day: Hypertrophy & Power" to Triple(3120, 6420.0, 440.0)),
            28 to ("Pull Day: Back & Biceps Strength" to Triple(3300, 7850.0, 470.0)),
            26 to ("Legs & Core Destruction" to Triple(2880, 8900.0, 490.0)),
            24 to ("Full Body Athletic Conditioning" to Triple(2520, 4800.0, 410.0)),
            22 to ("Push Day: Hypertrophy & Power" to Triple(3180, 6650.0, 450.0)),
            20 to ("Pull Day: Back & Biceps Strength" to Triple(3360, 8100.0, 480.0)),
            18 to ("Legs & Core Destruction" to Triple(3000, 9150.0, 510.0)),
            16 to ("Full Body Athletic Conditioning" to Triple(2460, 5100.0, 420.0)),
            14 to ("Push Day: Hypertrophy & Power" to Triple(3240, 6800.0, 460.0)),
            12 to ("Pull Day: Back & Biceps Strength" to Triple(3420, 8350.0, 495.0)),
            10 to ("Legs & Core Destruction" to Triple(3060, 9400.0, 525.0)),
            8 to ("Full Body Athletic Conditioning" to Triple(2580, 5300.0, 430.0)),
            6 to ("Push Day: Hypertrophy & Power" to Triple(3300, 7100.0, 475.0)),
            4 to ("Pull Day: Back & Biceps Strength" to Triple(3480, 8600.0, 510.0)),
            2 to ("Legs & Core Destruction" to Triple(3120, 9650.0, 540.0)),
            1 to ("Push Day: Hypertrophy & Power" to Triple(3200, 7250.0, 465.0))
        )

        for ((daysAgo, data) in sessionTemplates) {
            cal.time = baseDate
            cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
            val dateStr = sdf.format(cal.time)
            val routineName = data.first
            val (durationSec, volume, cals) = data.second

            histories.add(
                WorkoutHistory(
                    date = dateStr,
                    routineId = if (routineName.contains("Push")) 1L else if (routineName.contains("Pull")) 2L else if (routineName.contains("Legs")) 3L else 4L,
                    routineName = routineName,
                    durationSeconds = durationSec,
                    totalVolumeKg = volume,
                    caloriesBurned = cals,
                    completedExercisesCount = if (routineName.contains("Push") || routineName.contains("Pull")) 5 else 4,
                    timestamp = cal.timeInMillis + (17 * 3600 * 1000L) // logged at 5:00 PM
                )
            )
        }

        return histories
    }

    val starterChatMessages = listOf(
        ChatMessage(
            role = "user",
            content = "Hey Coach Alex! What's the optimal rest interval between heavy bench press sets?",
            timestamp = System.currentTimeMillis() - (3 * 86400 * 1000L)
        ),
        ChatMessage(
            role = "coach",
            content = "Great question, Alex! For heavy compound lifts in the 4-6 rep strength range (like Barbell Bench Press), rest **2 to 3 minutes**. This allows your ATP-CP energy system to replenish ~95% so you can maintain maximum mechanical tension. For accessory hypertrophy work (8-12 reps), 60-90 seconds is ideal.",
            timestamp = System.currentTimeMillis() - (3 * 86400 * 1000L) + 30000L
        ),
        ChatMessage(
            role = "user",
            content = "How much water should I be drinking on high volume leg days?",
            timestamp = System.currentTimeMillis() - (2 * 86400 * 1000L)
        ),
        ChatMessage(
            role = "coach",
            content = "Aim for **3.2 to 3.5 Liters** on high-demand training days. Drink 500ml upon waking, 500ml 1 hour before training, sip 700-1000ml with electrolytes during your session, and continue steady hydration post-workout to support muscle protein synthesis and glycogen reload.",
            timestamp = System.currentTimeMillis() - (2 * 86400 * 1000L) + 25000L
        ),
        ChatMessage(
            role = "user",
            content = "Should I take creatine before or after workouts?",
            timestamp = System.currentTimeMillis() - (1 * 86400 * 1000L)
        ),
        ChatMessage(
            role = "coach",
            content = "Consistency is key! 5g of Creatine Monohydrate daily at any time works because it relies on muscle saturation over time. However, taking it post-workout with your protein and carb meal gives slightly better uptake due to the insulin spike. Keep crushing your goals!",
            timestamp = System.currentTimeMillis() - (1 * 86400 * 1000L) + 20000L
        )
    )
}
