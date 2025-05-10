package com.app.calorie.data.database.repository

import com.app.calorie.data.database.AppDatabase
import com.app.calorie.data.database.relations.StatsWithDetails
import com.app.calorie.data.entities.Stats
import com.app.calorie.data.entities.Weight
import java.sql.Date
import java.time.LocalDate

class WeightRepository {

    suspend fun updateWeight(database: AppDatabase, weight: Double, goalWeight: Double, stat: StatsWithDetails?) {
        val weightId = database.weightDao().insertWeight(Weight(weight = weight, goalWeight = goalWeight))

        if (stat != null) {
            database.statsDao().updateStats(
                Stats(
                    id = stat.stats.id,
                    date = Date.valueOf(LocalDate.now().toString()),
                    weightId = weightId
                )
            )
        } else {
            database.statsDao().insertStats(
                Stats(
                    date = Date.valueOf(LocalDate.now().toString()),
                    weightId = weightId)
            )
        }

    }
}