package com.app.calorie.data.database.repository

import android.content.Context
import com.app.calorie.data.database.AppDatabase
import com.app.calorie.data.database.relations.StatsWithDetails
import com.app.calorie.data.database.relations.refs.FoodIntakeMealCrossRef
import com.app.calorie.data.entities.FoodIntake
import com.app.calorie.data.entities.Meal
import com.app.calorie.data.entities.Stats
import com.app.calorie.data.entities.Weight
import java.sql.Date
import java.time.LocalDate

class MealRepository {
//    suspend fun saveMeals(db: AppDatabase, foodIntakeName: String, mealName: String,
//                          protein: Double, carbs: Double, fat: Double,
//                          calories: Double, weight: Double, goalWeight: Double) {
////        val db = AppDatabase.getDatabase(context)
//
//        val foodIntakeId: Long
//        val stat: StatsWithDetails? = db.statsDao().getStatByDate(Date.valueOf(LocalDate.now().toString()))
//        val weightId = db.weightDao().insertWeight(Weight(weight = weight, goalWeight = goalWeight))
//
//        if(stat != null) {
//            val statsId = stat.stats.id
//            db.statsDao().updateStats(Stats(id = statsId, date = Date.valueOf(LocalDate.now().toString()), weightId = weightId))
//
//            val foodIntakeId = db.foodIntakeDao().getFoodIntake(foodIntakeName, statsId)
//
//            if (foodIntakeId != null) {
//                val newMeal = db.mealDao().insertMeal(createNewMeal(mealName,protein,carbs, fat,calories))
//                db.foodIntakeMealDao().insertCrossRef(FoodIntakeMealCrossRef(foodIntakeId.id, newMeal))
//            } else {
//                val foodIntakeId = db.foodIntakeDao().insertFoodIntake(FoodIntake(name = foodIntakeName, statsId = statsId))
//                val newMeal = db.mealDao().insertMeal(createNewMeal(mealName,protein,carbs, fat,calories))
//                db.foodIntakeMealDao().insertCrossRef(FoodIntakeMealCrossRef(foodIntakeId, newMeal))
//            }
//        } else {
//            val statsId = db.statsDao().insertStats(Stats(date = Date.valueOf(LocalDate.now().toString()), weightId = weightId))
//            foodIntakeId = db.foodIntakeDao().insertFoodIntake(FoodIntake(name = foodIntakeName, statsId = statsId))
//            val newMeal = db.mealDao().insertMeal(createNewMeal(mealName,protein,carbs, fat,calories))
//            db.foodIntakeMealDao().insertCrossRef(FoodIntakeMealCrossRef(foodIntakeId, newMeal))
//        }
//    }
    suspend fun saveMeals(
        db: AppDatabase,
        foodIntakeName: String,
        mealName: String,
        protein: Double,
        carbs: Double,
        fat: Double,
        calories: Double,
        weight: Double,
        goalWeight: Double
    ) {
        val mealDao = db.mealDao()
        val statsDao = db.statsDao()
        val weightDao = db.weightDao()
        val foodIntakeDao = db.foodIntakeDao()
        val crossRefDao = db.foodIntakeMealDao()
        val today = Date.valueOf(LocalDate.now().toString())

        val weightId = weightDao.insertWeight(Weight(weight = weight, goalWeight = goalWeight))
        val statWithDetails = statsDao.getStatByDate(today)

        val statsId = if (statWithDetails != null) {
            statsDao.updateStats(
                Stats(
                    id = statWithDetails.stats.id,
                    date = today,
                    weightId = weightId
                )
            )
            statWithDetails.stats.id
        } else {
            statsDao.insertStats(Stats(date = today, weightId = weightId))
        }

        val existingFoodIntake = foodIntakeDao.getFoodIntake(foodIntakeName, statsId)
        val foodIntakeId = existingFoodIntake?.id
            ?: foodIntakeDao.insertFoodIntake(FoodIntake(name = foodIntakeName, statsId = statsId))

        val mealId = mealDao.insertMeal(
            createNewMeal(mealName, protein, carbs, fat, calories)
        )

        crossRefDao.insertCrossRef(FoodIntakeMealCrossRef(foodIntakeId, mealId))
    }


//    fun getAllMeals(context: Context, date: Date): List<Meal> {
//        val db = AppDatabase.getDatabase(context)
//        return db.mealDao().getAllMeals(date)
//    }

    private fun createNewMeal(
                              mealName: String,
                              protein: Double,
                              carbs: Double,
                              fat: Double,
                              calories: Double): Meal {
        return Meal(
            name = mealName,
            date = Date.valueOf(LocalDate.now().toString()),
            protein = protein,
            carbs = carbs,
            fat = fat,
            calories = calories
        )
    }
}

