package com.app.calorie.utils

import androidx.room.TypeConverter
import java.sql.Date

class Converter {
    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(timestamp: Long): Date = Date(timestamp)

}