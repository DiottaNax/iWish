package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Likes
import com.unibo.rootly.data.database.daos.LikesDao

class LikesRepository(
    private val likesDao: LikesDao
) {
    suspend fun insert(likes: Likes) = likesDao.insertLikes(likes)

}