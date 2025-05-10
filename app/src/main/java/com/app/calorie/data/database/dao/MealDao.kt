package com.app.calorie.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.calorie.data.entities.Meal
import java.sql.Date

@Dao
interface MealDao {

    @Insert suspend fun insertMeal(meal: Meal): Long

    @Query("SELECT * FROM meal WHERE date =:date")
    fun getAllMeals(date: Date): List<Meal>
}
