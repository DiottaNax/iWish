package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Feeding
import com.unibo.rootly.data.database.daos.FeedingDao
import java.time.LocalDate

class FeedingRepository(
    private val feedingDao: FeedingDao
) {
    suspend fun insert(feeding: Feeding) = feedingDao.insertFeeding(feeding)

    fun getAll(userId: Int) = feedingDao.getAllWater(userId)

    fun getAllFavorites(userId: Int) = feedingDao.getAllWaterFavorites(userId)

    suspend fun remove(petId: Int, date: LocalDate) {
        feedingDao.removeWater(petId,date)
    }

    fun getLastWaterDate(petId: Int) = feedingDao.getLastWateredDate(petId)

    suspend fun getTimesWatered(userId:Int ) = feedingDao.getTimesWatered(userId)
}