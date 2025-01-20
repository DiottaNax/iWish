package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Cleaning
import com.unibo.rootly.data.database.daos.CleaningDao
import java.time.LocalDate

class CleaningRepository(
    private val cleaningDao: CleaningDao
) {
    suspend fun insert(fertilizer: Cleaning) = cleaningDao.insertCleaning(fertilizer)

    fun getAllFavorites(userId: Int) = cleaningDao.getAllFertilizeFavorites(userId)

    fun getAll(userId: Int) = cleaningDao.getAllFertilize(userId)

    suspend fun remove(plantId: Int, date: LocalDate) {
        cleaningDao.removeFertilize(plantId,date)
    }

    fun getLastFertilizeDate(plantId: Int) = cleaningDao.getLastFertilizeDate(plantId)

    fun getTimesFertilized(userId: Int) = cleaningDao.getTimesFertilized(userId)
}