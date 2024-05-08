package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.daos.PlantDao
import kotlinx.coroutines.flow.Flow

class PlantRepository(
    private val plantDao: PlantDao
) {
    suspend fun insert(plant: Plant) = plantDao.insertPlant(plant)

    suspend fun getByUser(userId: Int) : Flow<List<Plant>> = plantDao.getPlantsByUser(userId)
}