package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.User
import com.unibo.rootly.data.database.daos.UserDao

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun insert(user: User) :Long = userDao.insertUser(user)

    fun getUserByUsername(username: String) = userDao.getUserByUsername(username)

    fun getUserById(userId: Int): User? = userDao.getUserById(userId)
}