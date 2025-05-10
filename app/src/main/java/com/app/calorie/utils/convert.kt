package com.app.calorie.utils

fun convertActivityLevel(level: Double): String = when (level) {
    1.2 -> "Минимальная активность"
    1.375 -> "Легкие упражнения"
    1.55 -> "Умеренные упражнения"
    1.725 -> "Интенсивные упражнения"
    1.9 -> "Экстремальные нагрузки"
    else -> "Неизвестный уровень активности"
}

fun convertGender(gender: String): String = when (gender) {
    "male" -> "Мужчина"
    "female" -> "Женщина"
    "Мужчина".lowercase() -> "male"
    "Женщина".lowercase() -> "female"
    else -> " - "
}

fun isMale(gender: String): Boolean = when (gender) {
    "male" -> true
    else -> false
}

fun goalConverter(goal: Int): String = when (goal) {
    1 -> "Похудение"
    2 -> "Поддержание веса"
    3 -> "Набор массы"
    else -> " - "
}
