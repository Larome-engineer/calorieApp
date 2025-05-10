package com.app.calorie.data.database.relations

import com.app.calorie.data.entities.*
import androidx.room.Embedded
import androidx.room.Relation

data class StatsWithDetails(
    @Embedded val stats: Stats,

    @Relation(
        parentColumn = "weightId",
        entityColumn = "id"
    )
    val weight: Weight,

    @Relation(
        entity = FoodIntake::class,
        parentColumn = "id",
        entityColumn = "statsId"
    )
    val foodIntakes: List<FoodIntakeWithMeals>
)
