package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.User
import com.unibo.rootly.data.database.daos.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    @WorkerThread
    suspend fun insert(user: User) = userDao.insertUser(user)
    @WorkerThread
    fun getUserByUsername(username: String) = userDao.getUserByUsername(username)
}