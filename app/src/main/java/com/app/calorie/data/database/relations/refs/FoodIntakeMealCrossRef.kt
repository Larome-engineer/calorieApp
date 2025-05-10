package com.app.calorie.data.database.relations.refs

import androidx.room.Entity
import androidx.room.ForeignKey
import com.app.calorie.data.entities.FoodIntake
import com.app.calorie.data.entities.Meal

@Entity(
    primaryKeys = ["foodIntakeId", "mealId"],
    foreignKeys = [
        ForeignKey(
            entity = FoodIntake::class,
            parentColumns = ["id"],
            childColumns = ["foodIntakeId"],
            onDelete = ForeignKey.Companion.CASCADE,
            onUpdate = ForeignKey.Companion.CASCADE
        ),
        ForeignKey(
            entity = Meal::class,
            parentColumns = ["id"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.Companion.CASCADE,
            onUpdate = ForeignKey.Companion.CASCADE
        )
    ]
)
data class FoodIntakeMealCrossRef(
    val foodIntakeId: Long,
    val mealId: Long
)