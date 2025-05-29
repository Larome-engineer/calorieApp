package com.app.calorie.data.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.calorie.utils.Converter
import com.app.calorie.data.database.dao.FoodIntakeDao
import com.app.calorie.data.database.dao.FoodIntakeMealDao
import com.app.calorie.data.database.dao.MealDao
import com.app.calorie.data.database.dao.StatsDao
import com.app.calorie.data.database.dao.UserDao
import com.app.calorie.data.database.dao.WeightDao
import com.app.calorie.data.database.relations.refs.FoodIntakeMealCrossRef
import com.app.calorie.data.entities.FoodIntake
import com.app.calorie.data.entities.Meal
import com.app.calorie.data.entities.Stats
import com.app.calorie.data.entities.UserInfo
import com.app.calorie.data.entities.Weight

@Database(
    entities = [
        Meal::class,
        Weight::class,
        FoodIntake::class,
        FoodIntakeMealCrossRef::class,
        Stats::class,
        UserInfo::class
    ],
    version = 1
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun weightDao(): WeightDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun foodIntakeMealDao(): FoodIntakeMealDao
    abstract fun statsDao(): StatsDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "calorie_app_db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

