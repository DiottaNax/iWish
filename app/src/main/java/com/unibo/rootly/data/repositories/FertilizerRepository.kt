package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Fertilizer
import com.unibo.rootly.data.database.daos.FertilizerDao
import java.time.LocalDate
import javax.inject.Inject

class FertilizerRepository @Inject constructor(
    private val fertilizerDao: FertilizerDao
) {
    @WorkerThread
    suspend fun insert(fertilizer: Fertilizer) = fertilizerDao.insertFertilizer(fertilizer)

    @WorkerThread
    fun getSoon(userId: Int) = fertilizerDao.getSoonFertilizer(userId)

    @WorkerThread
    fun getToday(userId: Int) = fertilizerDao.getTodayFertilizer(userId)

    @WorkerThread
    fun getFavoritesSoon(userId: Int) = fertilizerDao.getFavoriteSoonFertilizer(userId)

    @WorkerThread
    fun getFavoritesToday(userId: Int) = fertilizerDao.getFavoriteTodayFertilizer(userId)
    @WorkerThread
    suspend fun remove(plantId: Int, date: LocalDate) {
        fertilizerDao.removeFertilize(plantId,date)
    }
}