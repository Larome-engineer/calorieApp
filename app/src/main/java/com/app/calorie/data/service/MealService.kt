package com.app.calorie.data.service

import android.content.Context
import android.util.Log
import com.app.calorie.data.database.AppDatabase
import com.app.calorie.data.database.repository.MealRepository

class MealService {

    private var mealRepo: MealRepository = MealRepository()

    suspend fun saveMeal(
        context: Context,
        foodIntakeName: String,
        mealName: String,
        protein: Double,
        carbs: Double,
        fat: Double,
        calories: Double,
        weight: Double,
        goalWeight: Double
    ) {
        try {
            val database = AppDatabase.getDatabase(context)
            mealRepo.saveMeals(database, foodIntakeName, mealName, protein, carbs, fat, calories, weight, goalWeight)
        } catch (e: Exception) {
            Log.e("Something went wrong", e.toString())
        }
    }
}