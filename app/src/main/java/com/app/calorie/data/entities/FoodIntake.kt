package com.app.calorie.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_intake")
data class FoodIntake(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // завтрак, обед и т.п.
    val statsId: Long // связь с конкретной статистикой (Stats)
)
