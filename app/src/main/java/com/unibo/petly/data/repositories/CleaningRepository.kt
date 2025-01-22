package com.unibo.petly.data.repositories

import com.unibo.petly.data.database.Cleaning
import com.unibo.petly.data.database.daos.CleaningDao
import java.time.LocalDate

class CleaningRepository(
    private val cleaningDao: CleaningDao
) {
    suspend fun insert(cleaning: Cleaning) = cleaningDao.insertCleaning(cleaning)

    fun getAllFavorites(userId: Int) = cleaningDao.getAllCleaningFavorites(userId)

    fun getAll(userId: Int) = cleaningDao.getAllCleaning(userId)

    suspend fun remove(petId: Int, date: LocalDate) {
        cleaningDao.removeCleaning(petId,date)
    }

    fun getLastCleaningDate(petId: Int) = cleaningDao.getLastCleaningDate(petId)

    fun getCleaningTimes(userId: Int) = cleaningDao.getCleaningTimes(userId)
}