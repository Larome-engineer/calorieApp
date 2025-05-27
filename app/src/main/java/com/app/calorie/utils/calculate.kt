data class MacroRecommendations(
    val maintenanceCalories: Int,  // текущий TDEE
    val targetCalories: Int,       // калории для достижения цели
    val proteinGrams: Int,
    val fatGrams: Int,
    val carbGrams: Int
)

fun calculateBMR(
    weightKg: Double,
    heightCm: Double,
    age: Int,
    isMale: Boolean
): Double = if (isMale) {
    10 * weightKg + 6.25 * heightCm - 5 * age + 5
} else {
    10 * weightKg + 6.25 * heightCm - 5 * age - 161
}

fun calculateTDEE(bmr: Double, activityMultiplier: Double): Double =
    bmr * activityMultiplier

fun calculateMacros(
    heightCm: Double,
    currentWeightKg: Double,
    targetWeightKg: Double,
    age: Int,
    isMale: Boolean,
    activityMultiplier: Double,
    weeksToAchieve: Int = 12
): MacroRecommendations {
    val days = weeksToAchieve * 7.0

    // Тут расчет TDEE на текущем весе
    val currentBmr  = calculateBMR(currentWeightKg, heightCm, age, isMale)
    val currentTdee = calculateTDEE(currentBmr, activityMultiplier)

    // Общее изменение калорий (цель – сейчас)
    val weightDiff        = targetWeightKg - currentWeightKg
    val kcalPerKgFat      = 7700.0
    val totalKcalChange   = weightDiff * kcalPerKgFat
    val dailyKcalDelta    = totalKcalChange / days

    // 3) Целевая калорийность
    val targetCaloriesD   = currentTdee + dailyKcalDelta

    // 4) Макро-разбивка
    // Белки: 1.8 г на кг текущего веса
    val proteinGramsD = currentWeightKg * 1.8

    // Жиры: 25% от целевых калорий
    val fatKcal       = targetCaloriesD * 0.25
    val fatGramsD     = fatKcal / 9.0

    // Углеводы: остаток калорий
    val remainingKcal = targetCaloriesD - (proteinGramsD * 4 + fatGramsD * 9)
    val carbGramsD    = remainingKcal / 4.0

    return MacroRecommendations(
        maintenanceCalories = currentTdee.toInt(),
        targetCalories      = targetCaloriesD.toInt(),
        proteinGrams        = proteinGramsD.toInt(),
        fatGrams            = fatGramsD.toInt(),
        carbGrams           = carbGramsD.toInt()
    )
}

