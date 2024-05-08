package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Fertilizer
import com.unibo.rootly.data.database.daos.FertilizerDao

class FertilizerRepository(
    private val fertilizerDao: FertilizerDao
) {
    suspend fun insert(fertilizer: Fertilizer) = fertilizerDao.insertFertilizer(fertilizer)

}