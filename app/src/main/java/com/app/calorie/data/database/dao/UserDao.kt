package com.app.calorie.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.calorie.data.entities.UserInfo

@Dao
interface UserDao {
    @Insert suspend fun createUser(user: UserInfo): Long

    @Query("SELECT * FROM user_info")
    suspend fun getAllInfo(): List<UserInfo>

}