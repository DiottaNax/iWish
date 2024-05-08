package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.daos.PlantDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlantRepository @Inject constructor(
    private val plantDao: PlantDao
) {
    @WorkerThread
    suspend fun insert(plant: Plant) = plantDao.insertPlant(plant)

    @WorkerThread
    suspend fun getByUser(userId: Int) : Flow<List<Plant>> = plantDao.getPlantsByUser(userId)
}