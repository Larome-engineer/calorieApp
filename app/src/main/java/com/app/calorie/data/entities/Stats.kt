package com.app.calorie.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    tableName = "stats",
    foreignKeys = [
        ForeignKey(entity = Weight::class, parentColumns = ["id"], childColumns = ["weightId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Stats(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Date,
    val weightId: Long
)
