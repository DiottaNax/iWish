package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.PlantLog
import com.unibo.rootly.data.database.daos.PlantLogDao
import kotlinx.coroutines.flow.Flow

class PlantLogRepository(
    private val plantLogDao: PlantLogDao
) {
    suspend fun insert(plantLog: PlantLog) = plantLogDao.insertPlantLog(plantLog)

    suspend fun getByPlant(userId: Int, plantId: Int)
            = plantLogDao.getPlantLogsByPlant(userId, plantId)

}