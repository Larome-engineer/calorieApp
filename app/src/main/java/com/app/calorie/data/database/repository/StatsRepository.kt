package com.app.calorie.data.database.repository

import com.app.calorie.data.database.AppDatabase
import com.app.calorie.data.database.relations.StatsWithDetails
import java.sql.Date

class StatsRepository {
    suspend fun getStatsByLastDate(database: AppDatabase): StatsWithDetails {
        return database.statsDao().getStatsByLastDate()
    }

    suspend fun getStatByDate(database: AppDatabase, date: Date): StatsWithDetails {
        return database.statsDao().getStatByDate(date)
    }

    suspend fun getAllDates(database: AppDatabase): List<Date> {
        return database.statsDao().getAllDates()
    }

}