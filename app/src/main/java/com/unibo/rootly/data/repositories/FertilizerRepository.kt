package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Fertilizer
import com.unibo.rootly.data.database.daos.FertilizerDao
import java.time.LocalDate

class FertilizerRepository(
    private val fertilizerDao: FertilizerDao
) {
    @WorkerThread
    suspend fun insert(fertilizer: Fertilizer) = fertilizerDao.insertFertilizer(fertilizer)

    @WorkerThread
    fun getDuesBeforeDateFavorites(userId: Int, date: LocalDate = LocalDate.now()) = fertilizerDao.getFertilizeBeforeDateFavorites(userId,date)

    @WorkerThread
    fun getAllFavorites(userId: Int) = fertilizerDao.getAllFertilizeFavorites(userId)

    @WorkerThread
    fun getDuesBeforeDate(userId: Int, date: LocalDate = LocalDate.now()) = fertilizerDao.getFertilizeBeforeDate(userId,date)

    @WorkerThread
    fun getAll(userId: Int) = fertilizerDao.getAllFertilize(userId)
    @WorkerThread
    suspend fun remove(plantId: Int, date: LocalDate) {
        fertilizerDao.removeFertilize(plantId,date)
    }

    @WorkerThread
    fun getLastFertilizeDate(plantId: Int) = fertilizerDao.getLastFertilizeDate(plantId)
    @WorkerThread
    fun getTimesFertilized(userId: Int) = fertilizerDao.getTimesFertilized(userId)
}