package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Feeding
import com.unibo.rootly.data.database.daos.FeedingDao
import java.time.LocalDate

class FeedingRepository(
    private val feedingDao: FeedingDao
) {
    suspend fun insert(feeding: Feeding) = feedingDao.insertFeeding(feeding)

    fun getAll(userId: Int) = feedingDao.getAllFeeding(userId)

    fun getAllFavorites(userId: Int) = feedingDao.getAllFeedingFavorites(userId)

    suspend fun remove(petId: Int, date: LocalDate) {
        feedingDao.removeFeeding(petId,date)
    }

    fun getLastFeedingDate(petId: Int) = feedingDao.getLastFeedingDate(petId)

    suspend fun getFeedingTimes(userId:Int ) = feedingDao.getFeedingTimes(userId)
}