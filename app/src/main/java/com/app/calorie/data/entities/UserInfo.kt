package com.app.calorie.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "user_info")
class UserInfo (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: Date,
    val age: Int,
    val height: Double,
    val activityLevel: Double,
    val gender: String,
    val goalIndex: Int // Похудение / Поддержание / Набор
)