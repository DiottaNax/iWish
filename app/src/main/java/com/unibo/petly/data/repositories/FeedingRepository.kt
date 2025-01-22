package com.unibo.petly.data.repositories

import com.unibo.petly.data.database.Feeding
import com.unibo.petly.data.database.daos.FeedingDao
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