package com.app.calorie.data.database.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.app.calorie.data.database.relations.refs.FoodIntakeMealCrossRef
import com.app.calorie.data.entities.FoodIntake
import com.app.calorie.data.entities.Meal

data class FoodIntakeWithMeals(
    @Embedded val foodIntake: FoodIntake,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = FoodIntakeMealCrossRef::class,
            parentColumn = "foodIntakeId",
            entityColumn = "mealId"
        )
    )
    val meals: List<Meal>
)
