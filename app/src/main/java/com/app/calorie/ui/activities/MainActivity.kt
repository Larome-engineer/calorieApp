package com.app.calorie.ui.activities


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import calculateMacros
import com.app.calorie.R
import com.app.calorie.data.database.relations.StatsWithDetails
import com.app.calorie.data.entities.Stats
import com.app.calorie.data.entities.UserInfo
import com.app.calorie.data.service.MealService
import com.app.calorie.data.service.StatsService
import com.app.calorie.data.service.UserService
import com.app.calorie.data.service.WeightService
import com.app.calorie.ui.custom.CustomMarkerView
import com.app.calorie.utils.convertActivityLevel
import com.app.calorie.utils.convertGender
import com.app.calorie.utils.goalConverter
import com.app.calorie.utils.isMale
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date

class MainActivity : AppCompatActivity() {

    private var userService = UserService()
    private var statsService  = StatsService()
    private var mealService = MealService()
    private var weightService = WeightService()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            val user: UserInfo?
            val users = userService.getUsers(applicationContext)
            user = if (!users.isEmpty()) {
                users.first()
            } else {
                null
            }
            val stats = statsService.getStatsByLastDate(applicationContext)
            calorieOnStartPreset(stats, user)
            presetClickButtonListeners()
        }
    }

    // CALORIE ON START & ON RESET
    @SuppressLint("SetTextI18n")
    fun calorieOnStartPreset(stats: StatsWithDetails?, user: UserInfo?) {
        if (stats == null && user == null) {
            setupPieChart(0, 0)
            setupLineChart(0, 0, "carbs")
            setupLineChart(0, 0, "protein")
            setupLineChart(0, 0, "fat")
            findViewById<TextView>(R.id.weightText).text = 0.0.toString()
            findViewById<TextView>(R.id.goalWeightText).text = 0.0.toString()
        } else {
            val userInfo: String = if (user == null) {
                ""
            } else {
                "• Имя: ${user.name}\n" +
                        "• Рост: ${user.height}\n" +
                        "• Возраст: ${user.age}\n" +
                        "• Пол: ${convertGender(user.gender)}\n" +
                        "• Активность: ${convertActivityLevel(user.activityLevel)}\n" +
                        "• Цель: ${goalConverter(user.goalIndex)}"
            }

            findViewById<TextView>(R.id.userInfo).apply {
                text = userInfo
                textSize = 10f
            }


            if (stats == null && user!=null) {
                val calories = calculateMacros(
                    user.height.toDouble(),
                    0.0,
                    0.0,
                    user.age,
                    isMale(user.gender),
                    user.activityLevel
                )

                setupPieChart(0, calories.targetCalories.toInt())
                setupLineChart(0, calories.carbGrams, "carbs")
                setupLineChart(0, calories.proteinGrams, "protein")
                setupLineChart(0, calories.fatGrams, "fat")

                findViewById<TextView>(R.id.weightText).text = 0.0.toString()
                findViewById<TextView>(R.id.goalWeightText).text = 0.0.toString()

            } else if (stats != null && user!=null){
                val sumOfCalories: Int = stats.foodIntakes.sumOf { intake ->
                    intake.meals.sumOf { meal ->
                        meal.calories.toInt()
                    }
                }
                val sumOfProtein: Int = stats.foodIntakes.sumOf { intake ->
                    intake.meals.sumOf { meal ->
                        meal.protein.toInt()
                    }
                }
                val sumOfCarbs: Int = stats.foodIntakes.sumOf { intake ->
                    intake.meals.sumOf { meal ->
                        meal.carbs.toInt()
                    }
                }
                val sumOfFat: Int = stats.foodIntakes.sumOf { intake ->
                    intake.meals.sumOf { meal ->
                        meal.fat.toInt()
                    }
                }

                val mealsByFoodIntake = mutableMapOf<String, List<String>>()
                stats.foodIntakes.forEach { intake ->
                    val mealNames = intake.meals.map { it.name}
                    mealsByFoodIntake[intake.foodIntake.name] = mealNames
                }

                findViewById<TextView>(R.id.breakfastMeals).text =
                    mealsByFoodIntake["Завтрак"]?.joinToString(", ", prefix = "", postfix = "") ?: ""

                findViewById<TextView>(R.id.lunchMeals).text =
                    mealsByFoodIntake["Обед"]?.joinToString(", ", prefix = "", postfix = "") ?: ""

                findViewById<TextView>(R.id.dinnerMeals).text =
                    mealsByFoodIntake["Ужин"]?.joinToString(", ", prefix = "", postfix = "") ?: ""


                val calories = calculateMacros(
                    heightCm = user.height.toDouble(),
                    currentWeightKg = stats.weight.weight,
                    targetWeightKg = stats.weight.goalWeight,
                    age = user.age,
                    isMale = isMale(user.gender),
                    activityMultiplier = user.activityLevel
                )

                setupPieChart(sumOfCalories, calories.targetCalories.toInt())
                setupLineChart(sumOfCarbs, calories.carbGrams, "carbs")
                setupLineChart(sumOfProtein, calories.proteinGrams, "protein")
                setupLineChart(sumOfFat, calories.fatGrams, "fat")

                findViewById<TextView>(R.id.weightText).text = stats.weight.weight.toString()
                findViewById<TextView>(R.id.goalWeightText).text = stats.weight.goalWeight.toString()

            }

        }
    }


    private fun reloadCalories(stats: StatsWithDetails?, user: UserInfo?) {
        if (user == null) {
            return
        } else {
            val userInfo = "• Имя: ${user.name}\n" +
                    "• Рост: ${user.height}\n" +
                    "• Возраст: ${user.age}\n" +
                    "• Пол: ${convertGender(user.gender)}\n" +
                    "• Активность: ${convertActivityLevel(user.activityLevel)}\n" +
                    "• Цель: ${goalConverter(user.goalIndex)}"

            findViewById<TextView>(R.id.userInfo).apply {
                text = userInfo
                textSize = 10f
            }

            if (stats == null) {
                val calories = calculateMacros(
                    heightCm = user.height.toDouble(),
                    currentWeightKg = 0.0,
                    targetWeightKg = 0.0,
                    age = user.age,
                    isMale = isMale(user.gender),
                    activityMultiplier = user.activityLevel
                )

                setupPieChart(0, calories.targetCalories.toInt())
                setupLineChart(0, calories.carbGrams, "carbs")
                setupLineChart(0, calories.proteinGrams, "protein")
                setupLineChart(0, calories.fatGrams, "fat")

                findViewById<TextView>(R.id.weightText).text = 0.0.toString()
                findViewById<TextView>(R.id.goalWeightText).text = 0.0.toString()

            } else {
                val sumOfCalories: Int = stats.foodIntakes.sumOf { intake ->
                    intake.meals.sumOf { meal ->
                        meal.calories.toInt()
                    }
                }
                val sumOfProtein: Int = stats.foodIntakes.sumOf { intake ->
                    intake.meals.sumOf { meal ->
                        meal.protein.toInt()
                    }
                }
                val sumOfCarbs: Int = stats.foodIntakes.sumOf { intake ->
                    intake.meals.sumOf { meal ->
                        meal.carbs.toInt()
                    }
                }
                val sumOfFat: Int = stats.foodIntakes.sumOf { intake ->
                    intake.meals.sumOf { meal ->
                        meal.fat.toInt()
                    }
                }

                val mealsByFoodIntake = mutableMapOf<String, List<String>>()
                stats.foodIntakes.forEach { intake ->
                    val mealNames = intake.meals.map { it.name}
                    mealsByFoodIntake[intake.foodIntake.name] = mealNames
                }

                val calories = calculateMacros(
                    heightCm = user.height.toDouble(),
                    currentWeightKg = stats.weight.weight,
                    targetWeightKg = stats.weight.goalWeight,
                    age = user.age,
                    isMale = isMale(user.gender),
                    activityMultiplier = user.activityLevel
                )

                findViewById<TextView>(R.id.breakfastMeals).text =
                    mealsByFoodIntake["Завтрак"]?.joinToString(", ", prefix = "", postfix = "") ?: ""

                findViewById<TextView>(R.id.lunchMeals).text =
                    mealsByFoodIntake["Обед"]?.joinToString(", ", prefix = "", postfix = "") ?: ""

                findViewById<TextView>(R.id.dinnerMeals).text =
                    mealsByFoodIntake["Ужин"]?.joinToString(", ", prefix = "", postfix = "") ?: ""

                setupPieChart(sumOfCalories, calories.targetCalories.toInt(), "reset")
                setupLineChart(sumOfCarbs, calories.carbGrams, "carbs")
                setupLineChart(sumOfProtein, calories.proteinGrams, "protein")
                setupLineChart(sumOfFat, calories.fatGrams, "fat")

                findViewById<TextView>(R.id.weightText).text = stats.weight.weight.toString()
                findViewById<TextView>(R.id.goalWeightText).text = stats.weight.goalWeight.toString()
            }
        }

    }

    // SHOW DIALOGS
    @SuppressLint("InflateParams")
    private fun showAddMealDialog(foodIntake: String) {
        val dialogView = layoutInflater.inflate(R.layout.meal_dialog_add, null)
        val dishContainer = dialogView.findViewById<LinearLayout>(R.id.dishInputContainer)
        val buttonAddDish = dialogView.findViewById<Button>(R.id.buttonAddDish)

        fun addDishInput() {
            val dishInput = layoutInflater.inflate(R.layout.meal_item_input, dishContainer, false)
            dishContainer.addView(dishInput)
        }

        addDishInput()

        buttonAddDish.setOnClickListener {
            addDishInput()
        }

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogView)

        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonSave.setOnClickListener {
            val context = applicationContext
            for (i in 0 until dishContainer.childCount) {
                val view = dishContainer.getChildAt(i)

                val name = view.findViewById<EditText>(R.id.editDishName).text.toString()

                if (name.isNotBlank()) {
                    lifecycleScope.launch {
                        if (!userService.getUsers(context).isEmpty()) {
                            mealService.saveMeal(
                                applicationContext,
                                foodIntake,
                                name,
                                view.findViewById<EditText>(R.id.editProtein).text.toString().toDoubleOrNull() ?: 0.0,
                                view.findViewById<EditText>(R.id.editCarbs).text.toString().toDoubleOrNull() ?: 0.0,
                                view.findViewById<EditText>(R.id.editFat).text.toString().toDoubleOrNull() ?: 0.0,
                                view.findViewById<EditText>(R.id.editCalories).text.toString().toDoubleOrNull() ?: 0.0,
                                findViewById<TextView>(R.id.weightText).text.toString().toDoubleOrNull() ?: 0.0,
                                findViewById<TextView>(R.id.goalWeightText).text.toString().toDoubleOrNull() ?: 0.0,
                            )
                            reloadCalories(
                                statsService.getStatsByLastDate(context),
                                userService.getUsers(context).first()
                            )
                        }
                    }
                }
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun showAddWeightDialog() {
        val context = applicationContext
        val dialogView = layoutInflater.inflate(R.layout.weight_dialog_add, null)
        val weightContainer = dialogView.findViewById<LinearLayout>(R.id.weightInputContainer)

        val userInput = layoutInflater.inflate(R.layout.weight_item_input, weightContainer, false)
        weightContainer.addView(userInput)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogView)

        val buttonSave = dialogView.findViewById<Button>(R.id.buttonWeightSave)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonWeightCancel)

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonSave.setOnClickListener {
            lifecycleScope.launch {
                val user: List<UserInfo> = userService.getUsers(context)
                if (!user.isEmpty()) {
                    val view = weightContainer.getChildAt(0)

                    weightService.updateWeight(
                        context,
                        view.findViewById<EditText>(R.id.editWeight).text.toString().toString().toDoubleOrNull() ?: 0.0,
                        findViewById<TextView>(R.id.goalWeightText).text.toString().toDoubleOrNull() ?:0.0)

                    reloadCalories(
                        statsService.getStatsByLastDate(context),
                        userService.getUsers(context).first()
                    )
                }
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun showAddGoalWeightDialog() {
        val context = applicationContext
        val dialogView = layoutInflater.inflate(R.layout.weight_goal_dialog_add, null)
        val weightContainer = dialogView.findViewById<LinearLayout>(R.id.weightGoalInputContainer)

        val userInput = layoutInflater.inflate(R.layout.weight_goal_item_input, weightContainer, false)
        weightContainer.addView(userInput)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogView)

        val buttonSave = dialogView.findViewById<Button>(R.id.buttonGoalWeightSave)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonGoalWeightCancel)

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonSave.setOnClickListener {
            buttonSave.setOnClickListener {
                lifecycleScope.launch {
                    val user: List<UserInfo> = userService.getUsers(context)
                    if (!user.isEmpty()) {
                        val view = weightContainer.getChildAt(0)

                        weightService.updateWeight(
                            context,
                            findViewById<TextView>(R.id.weightText).text.toString().toDoubleOrNull() ?:0.0,
                            view.findViewById<EditText>(R.id.editGoalWeight).text.toString().toString().toDoubleOrNull() ?: 0.0
                        )

                        // Вот тут обновить покзатели
                        reloadCalories(
                            statsService.getStatsByLastDate(context),
                            userService.getUsers(applicationContext).first()
                        )
                    }
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }



    @SuppressLint("InflateParams")
    private fun showAddUserDialog() {
        val context = applicationContext

        val dialogView = layoutInflater.inflate(R.layout.user_dialog_add, null)
        val userContainer = dialogView.findViewById<LinearLayout>(R.id.userInputContainer)

        val userInput = layoutInflater.inflate(R.layout.user_item_input, userContainer, false)
        userContainer.addView(userInput)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogView)

        val buttonSave = dialogView.findViewById<Button>(R.id.buttonUserSave)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonUserCancel)


        radioButtonInit(arrayOf("male", "female"), userInput.findViewById<EditText>(R.id.editGender))
        radioButtonInit(arrayOf("1.2", "1.375", "1.55", "1.725", "1.9"), userInput.findViewById<EditText>(R.id.editActivity))
        radioButtonInit(arrayOf("1", "2", "3"), userInput.findViewById<EditText>(R.id.editGoalIndex))

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonSave.setOnClickListener {
            lifecycleScope.launch {
                val user: List<UserInfo> = userService.getUsers(context)
                if (user.isEmpty()) {
                    val view = userContainer.getChildAt(0)

                    val name = view.findViewById<EditText>(R.id.editUserName).text.toString()
                    val age = view.findViewById<EditText>(R.id.editUserAge).text.toString().toIntOrNull() ?: 0
                    val height = view.findViewById<EditText>(R.id.editHeight).text.toString().toDoubleOrNull() ?: 0.0
                    val activityLevel = view.findViewById<EditText>(R.id.editActivity).text.toString().toDoubleOrNull() ?: 0.0
                    val gender = view.findViewById<EditText>(R.id.editGender).text.toString()
                    val goalIndex = view.findViewById<EditText>(R.id.editGoalIndex).text.toString().toIntOrNull() ?: 0

                    userService.createUser(context, name, age, height, activityLevel, gender, goalIndex)

                    reloadCalories(
                        statsService.getStatsByLastDate(context),
                        userService.getUsers(context).first()
                    )
                }
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun radioButtonInit(options: Array<String>, view: EditText) {
        view.setOnClickListener {
            var selectedOption = -1

            AlertDialog.Builder(this)
                .setTitle("Выберите опцию")
                .setSingleChoiceItems(options, selectedOption) { _, which ->
                    selectedOption = which
                }
                .setPositiveButton("ОК") { dialog, _ ->
                    if (selectedOption >= 0) {
                        view.setText(options[selectedOption])
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
                .show()
        }

    }

    // BUTTONS CLICK LISTENERS
    private fun presetClickButtonListeners() {
        val breakfastAddButton: Button = findViewById<Button>(R.id.btnAddBrst)
        val lunchAddButton: Button = findViewById<Button>(R.id.btnAddLunch)
        val dinnerAddButton: Button = findViewById<Button>(R.id.btnAddDinner)
        val userInfoButton: Button = findViewById<Button>(R.id.userInfoBtn)
        val dateButton: Button = findViewById<Button>(R.id.titleButton)
        val changeWeightButton: Button = findViewById<Button>(R.id.changeWeight)
        val changeGoalWeightButton: Button = findViewById<Button>(R.id.changeGoalWeight)

        dateButton.text = "Сегодня"

        breakfastAddButton.setOnClickListener {
            showAddMealDialog("Завтрак")
        }

        lunchAddButton.setOnClickListener {
            showAddMealDialog("Обед")
        }

        dinnerAddButton.setOnClickListener {
            showAddMealDialog("Ужин")
        }

        userInfoButton.setOnClickListener {
            showAddUserDialog()
        }

        changeWeightButton.setOnClickListener {
            showAddWeightDialog()
        }

        changeGoalWeightButton.setOnClickListener {
            showAddGoalWeightDialog()
        }

        dateButton.setOnClickListener {
            val popupMenu = PopupMenu(this@MainActivity, dateButton)

            lifecycleScope.launch {
                val context = applicationContext
                try {
                    val allDates = statsService.getAllDates(context)

                    if (allDates.isEmpty()) {
                        showError("Сохраненной статистики нет")

                    } else {
                        val dates = allDates.map { it.toString() }
                        withContext(Dispatchers.Main) {
                            dates.forEachIndexed { index, date ->
                                popupMenu.menu.add(0, index, index, date)
                            }

                            popupMenu.setOnMenuItemClickListener { menuItem ->
                                lifecycleScope.launch {
                                    try {
                                        val selectedDate = Date.valueOf(dates[menuItem.itemId])
                                        val stat = statsService.getStatsByDate(context, selectedDate)

                                        reloadCalories(stat, userService.getUsers(context).first())

                                        stat?.let {
                                            showStats(stat.stats)
                                        } ?: showError("Данные не найдены")

                                    } catch (e: IllegalArgumentException) {
                                        showError("Некорректный формат даты: ${e.localizedMessage}")
                                    } catch (e: Exception) {
                                        showError("Ошибка: ${e.localizedMessage}")
                                    }
                                }
                                true
                            }

                            popupMenu.show()
                        }

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Ошибка загрузки дат: ${e.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    private fun showStats(stat: Stats) {
        Toast.makeText(this, "Данные за ${stat.date}", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //SETUP PIE AND LINE CHARTS
    @SuppressLint("SetTextI18n")
    private fun setupLineChart(consumed: Int, remaining: Int, parameter: String) {
        val (progressBarId, textViewId) = when (parameter) {
            "protein" -> R.id.proteinProgress to R.id.proteinValue
            "carbs" -> R.id.carbsProgress to R.id.carbsValue
            "fat" -> R.id.fatProgress to R.id.fatValue
            else -> return
        }

        val progressBar = findViewById<ProgressBar>(progressBarId)
        val valueTextView = findViewById<TextView>(textViewId)

        progressBar.max = remaining
        progressBar.progress = consumed

        valueTextView.apply {
            text = "$consumed / $remaining г"
            textSize = 10f
        }
    }

    private fun setupPieChart(consumed: Int, needed: Int, flag: String = "new") {
        val pieChart = findViewById<PieChart>(R.id.pieChart)
        if (flag == "reset") {
            pieChart.clear()
        }
        val entries = ArrayList<PieEntry>()
        val dataSet = PieDataSet(entries, "")
        val data = PieData(dataSet)


        entries.add(PieEntry(consumed.toFloat(), "Съедено"))
        entries.add(PieEntry((needed - consumed).toFloat(), "Осталось"))

        dataSet.colors = listOf(
            "#3b82f6".toColorInt(),
            Color.WHITE
        )
        dataSet.setDrawValues(false)

        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.holeRadius = 90f
        pieChart.transparentCircleRadius = 0f
        pieChart.centerText = "${needed - consumed}\nОсталось"

        pieChart.setUsePercentValues(false)
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setDrawEntryLabels(false)
        pieChart.setDrawCenterText(true)
        pieChart.setCenterTextColor(Color.WHITE)
        pieChart.setCenterTextSize(15f)
        pieChart.animateY(1000, Easing.EaseInOutQuad)
        val markerView = CustomMarkerView(this, R.layout.marker_view)
        markerView.chartView = pieChart
        pieChart.marker = markerView

    }
}
