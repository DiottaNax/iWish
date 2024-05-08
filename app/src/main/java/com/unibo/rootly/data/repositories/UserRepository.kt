package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.User
import com.unibo.rootly.data.database.daos.UserDao

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun insert(user: User) = userDao.insertUser(user)
    suspend fun getUserByUsername(username: String) = userDao.getUserByUsername(username)
}