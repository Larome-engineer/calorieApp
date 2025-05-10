package com.app.calorie.data.service

import android.content.Context
import android.util.Log
import com.app.calorie.data.database.AppDatabase
import com.app.calorie.data.database.relations.StatsWithDetails
import com.app.calorie.data.database.repository.StatsRepository
import java.sql.Date

class StatsService {

    private var statRepo: StatsRepository = StatsRepository()

    suspend fun getStatsByLastDate(context: Context): StatsWithDetails? {
        try {
            return statRepo.getStatsByLastDate(AppDatabase.getDatabase(context))
        } catch (e: Exception) {
            Log.e("Something went wrong", e.toString())
            return null
        }
    }

    suspend fun getStatsByDate(context: Context, date: Date): StatsWithDetails? {
        try {
            return statRepo.getStatByDate(AppDatabase.getDatabase(context), date)
        } catch (e: Exception) {
            Log.e("Something went wrong", e.toString())
            return null
        }
    }

    suspend fun getAllDates(context: Context): List<Date> {
        try {
            val dates = statRepo.getAllDates(AppDatabase.getDatabase(context))
            if (dates.isEmpty()) {
                return emptyList()
            }
            return dates
        } catch (e: Exception) {
            Log.e("Something went wrong", e.toString())
            return emptyList()
        }
    }

}