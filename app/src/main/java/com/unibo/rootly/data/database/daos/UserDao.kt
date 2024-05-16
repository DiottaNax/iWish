package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM Users WHERE username = :username")
    fun getUserByUsername(username: String): User?
    @Query("SELECT * FROM Users WHERE user_id = :userId")
    fun getUserById(userId: Int): User?

    @Query("UPDATE users SET profile_img = :uri WHERE user_id = :id")
    fun setProPic(id: Int, uri: String)
}