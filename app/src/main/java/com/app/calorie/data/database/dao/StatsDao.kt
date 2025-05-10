package com.app.calorie.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.app.calorie.data.database.relations.StatsWithDetails
import com.app.calorie.data.entities.Stats
import java.sql.Date

@Dao
interface StatsDao {
    @Insert suspend fun insertStats(stats: Stats): Long

    @Update suspend fun updateStats(stats: Stats): Int

    @Transaction
    @Query("SELECT * FROM stats ORDER BY date DESC LIMIT 1")
    suspend fun getStatsByLastDate(): StatsWithDetails

    @Transaction
    @Query("SELECT * FROM stats WHERE date =:date")
    suspend fun getStatByDate(date: Date): StatsWithDetails

    @Transaction
    @Query("SELECT date FROM stats")
    suspend fun getAllDates(): List<Date>
}
