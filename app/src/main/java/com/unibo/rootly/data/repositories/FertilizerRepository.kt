package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Fertilizer
import com.unibo.rootly.data.database.daos.FertilizerDao
import javax.inject.Inject

class FertilizerRepository @Inject constructor(
    private val fertilizerDao: FertilizerDao
) {
    @WorkerThread
    suspend fun insert(fertilizer: Fertilizer) = fertilizerDao.insertFertilizer(fertilizer)
}