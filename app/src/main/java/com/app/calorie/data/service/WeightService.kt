package com.app.calorie.data.service

import android.content.Context
import android.util.Log
import com.app.calorie.data.database.AppDatabase
import com.app.calorie.data.database.relations.StatsWithDetails
import com.app.calorie.data.database.repository.WeightRepository
import java.sql.Date
import java.time.LocalDate

class WeightService {

    private var weightRepository = WeightRepository()
    private var statService = StatsService()

    suspend fun updateWeight(context: Context, weight: Double, goalWeight: Double) {
        try {
            val stat: StatsWithDetails? = statService.getStatsByDate(context, Date.valueOf(LocalDate.now().toString()))
            weightRepository.updateWeight(AppDatabase.getDatabase(context), weight, goalWeight, stat)
        } catch (e: Exception) {
            Log.e("Something went wrong", e.toString())
        }

    }

}