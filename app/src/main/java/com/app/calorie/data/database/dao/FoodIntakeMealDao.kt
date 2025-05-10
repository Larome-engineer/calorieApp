package com.app.calorie.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.app.calorie.data.database.relations.refs.FoodIntakeMealCrossRef

@Dao
interface FoodIntakeMealDao {

    @Insert suspend fun insertCrossRef(crossRef: FoodIntakeMealCrossRef)
}
