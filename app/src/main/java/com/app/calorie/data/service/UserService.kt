package com.app.calorie.data.service

import android.content.Context
import android.util.Log
import com.app.calorie.data.database.AppDatabase
import com.app.calorie.data.database.repository.UserRepository
import com.app.calorie.data.entities.UserInfo

class UserService {

    private var userRepo: UserRepository = UserRepository()

    suspend fun createUser(
        context: Context,
        name: String,
        age: Int,
        height: Double,
        activityLevel: Double,
        gender: String,
        goalIndex: Int
    ) {
        try {
            userRepo.createUser(AppDatabase.getDatabase(context), name, age, height, activityLevel, gender, goalIndex)
        } catch (e: Exception) {
            Log.e("Something went wrong", e.toString())
        }
    }

    suspend fun getUsers(context: Context): List<UserInfo> {
        try {
            return userRepo.getUsers(AppDatabase.getDatabase(context))
        } catch (e: Exception) {
            return emptyList()
            Log.e("Something went wrong", e.toString())
        }
    }
}