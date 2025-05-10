package com.app.calorie.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "meal")
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: Date,
    val protein: Double,
    val carbs: Double,
    val fat: Double,
    val calories: Double
)