package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Likes
import com.unibo.rootly.data.database.daos.LikesDao
import javax.inject.Inject

class LikesRepository @Inject constructor(
    private val likesDao: LikesDao
) {
    @WorkerThread
    suspend fun insert(likes: Likes) = likesDao.insertLikes(likes)
}