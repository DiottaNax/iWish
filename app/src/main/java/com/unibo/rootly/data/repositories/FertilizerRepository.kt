package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Fertilizer

class FertilizerRepository(
    private val fertilizerDao: FertilizerDao
) {
    suspend fun insert(fertilizer: Fertilizer) = fertilizerDao.insertFertilizer(fertilizer)

}