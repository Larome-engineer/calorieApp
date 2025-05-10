
// Calculate Calories
fun calculateBMR(weight: Double, height: Int, age: Int, isMale: Boolean): Double {
    return if (isMale) {
        10 * weight + 6.25 * height - 5 * age + 5
    } else {
        10 * weight + 6.25 * height - 5 * age - 161
    }
}

fun calculateTDEE(bmr: Double, activityLevel: Double): Double {
    return bmr * activityLevel
}

fun calculateCaloriesForTargetWeight(
    currentWeight: Double,
    targetWeight: Double,
    height: Int,
    age: Int,
    isMale: Boolean,
    activityLevel: Double,
    weeksToAchieve: Int = 12


): DailyCalorieRecommendation {
    // Рассчитаем BMR для целевого веса
    val targetBMR = calculateBMR(targetWeight, height, age, isMale)
    val targetTDEE = calculateTDEE(targetBMR, activityLevel)

    // Разница в весе
    val weightDifference = currentWeight - targetWeight
    val caloriesPerKg = 7700 // Столько калорий в 1 кг жира

    // Расчет общего дефицита/профицита
    val totalCalorieDifference = weightDifference * caloriesPerKg

    // Дневная разница
    val dailyDifference = totalCalorieDifference / (weeksToAchieve * 7)

    // Рекомендуемые калории
    val recommendedCalories = targetTDEE + dailyDifference

    return DailyCalorieRecommendation(
        maintenanceCalories = targetTDEE.toInt(),
        recommendedCalories = recommendedCalories.toInt(),
        dailyDifference = dailyDifference.toInt(),
        weeksToAchieve = weeksToAchieve
    )
}

data class DailyCalorieRecommendation(
    val maintenanceCalories: Int,
    val recommendedCalories: Int,
    val dailyDifference: Int,
    val weeksToAchieve: Int
)