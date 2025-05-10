package com.app.calorie.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.calorie.data.entities.Weight

@Dao
interface WeightDao {
    @Insert suspend fun insertWeight(weight: Weight): Long
    @Query("SELECT * FROM weight") fun getAllWeight(): LiveData<List<Weight>>
}
