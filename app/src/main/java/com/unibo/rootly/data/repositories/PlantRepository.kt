package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.daos.PlantDao

class PlantRepository(
    private val plantDao: PlantDao
) {
    @WorkerThread
    suspend fun insert(plant: Plant) = plantDao.insertPlant(plant)

    @WorkerThread
    suspend fun insertDead(plantId: Int) = plantDao.insertDead(plantId)

    @WorkerThread
    suspend fun insertLike(plantId: Int) = plantDao.insertLike(plantId)
    @WorkerThread
    suspend fun removeLike(plantId: Int) = plantDao.removeLike(plantId)
    @WorkerThread
    suspend fun countByUser(userId: Int): Int? = plantDao.countByUser(userId)

}