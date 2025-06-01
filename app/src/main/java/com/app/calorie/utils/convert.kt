package com.app.calorie.utils

fun convertActivityLevelToValue(level: Double): String = when (level) {
    1.2 -> "Минимальная активность"
    1.375 -> "Легкие упражнения"
    1.55 -> "Умеренные упражнения"
    1.725 -> "Интенсивные упражнения"
    1.9 -> "Экстремальные нагрузки"
    else -> throw IllegalArgumentException()
}

fun convertGenderToValue(gender: String): String = when (gender) {
    "male" -> "Мужчина"
    "female" -> "Женщина"
    "Мужчина" -> "male"
    "Женщина" -> "female"
    else -> throw IllegalArgumentException()
}

fun isMale(gender: String): Boolean = when (gender) {
    "male" -> true
    else -> false
}

fun convertGoalToValue(goal: Int): String = when (goal) {
    1 -> "Похудение"
    2 -> "Поддержание веса"
    3 -> "Набор массы"
    else -> throw IllegalArgumentException()
}

fun convertValueToGoal(value: String): Int = when (value) {
    "Похудение" -> 1
    "Поддержание веса" -> 2
    "Набор массы" -> 3
    else -> throw IllegalArgumentException()
}

fun convertFromValueToActivityLevel(value: String): Double = when(value) {
    "Минимальная активность" -> 1.2
    "Легкие упражнения" -> 1.375
    "Умеренные упражнения" -> 1.55
    "Интенсивные упражнения" -> 1.725
    "Экстремальные нагрузки" -> 1.9
    else -> throw IllegalArgumentException()
}
