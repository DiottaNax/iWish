package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Water
import com.unibo.rootly.data.database.daos.WaterDao
import javax.inject.Inject

class WaterRepository @Inject constructor(
    private val waterDao: WaterDao
) {
    @WorkerThread
    suspend fun insert(water: Water) = waterDao.insertWater(water)
}