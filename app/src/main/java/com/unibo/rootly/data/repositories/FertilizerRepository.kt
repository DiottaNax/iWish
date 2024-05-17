package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Fertilizer
import com.unibo.rootly.data.database.daos.FertilizerDao
import java.time.LocalDate

class FertilizerRepository(
    private val fertilizerDao: FertilizerDao
) {
    suspend fun insert(fertilizer: Fertilizer) = fertilizerDao.insertFertilizer(fertilizer)

    fun getAllFavorites(userId: Int) = fertilizerDao.getAllFertilizeFavorites(userId)

    fun getAll(userId: Int) = fertilizerDao.getAllFertilize(userId)

    suspend fun remove(plantId: Int, date: LocalDate) {
        fertilizerDao.removeFertilize(plantId,date)
    }

    fun getLastFertilizeDate(plantId: Int) = fertilizerDao.getLastFertilizeDate(plantId)

    fun getTimesFertilized(userId: Int) = fertilizerDao.getTimesFertilized(userId)
}