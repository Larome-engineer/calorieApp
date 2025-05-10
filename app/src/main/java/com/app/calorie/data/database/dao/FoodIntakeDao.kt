package com.app.calorie.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.app.calorie.data.entities.FoodIntake


@Dao
interface FoodIntakeDao {
    @Insert suspend fun insertFoodIntake(foodIntake: FoodIntake): Long

    @Update suspend fun updateFoodIntake(foodIntake: FoodIntake): Int

    @Query("SELECT * FROM food_intake where name =:name and statsId =:statsId")
    suspend fun getFoodIntake(name: String, statsId: Long): FoodIntake
}
