package com.app.calorie.data.database.repository

import android.content.Context
import com.app.calorie.data.database.AppDatabase
import com.app.calorie.data.entities.UserInfo
import java.sql.Date
import java.time.LocalDate

class UserRepository {

    suspend fun createUser(
        database: AppDatabase,
        name: String,
        age: Int,
        height: Double,
        activityLevel: Double, gender: String,
        goalIndex: Int
    ) {
        database.userDao().createUser(UserInfo(
            name = name,
            date = Date.valueOf(LocalDate.now().toString()),
            age = age,
            height = height,
            activityLevel = activityLevel,
            gender = gender,
            goalIndex = goalIndex
        ))
    }

    suspend fun getUsers(database: AppDatabase): List<UserInfo> {
        return database.userDao().getAllInfo()
    }

}