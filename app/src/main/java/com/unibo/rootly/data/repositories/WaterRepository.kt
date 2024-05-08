package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Water
import com.unibo.rootly.data.database.daos.WaterDao

class WaterRepository(
    private val waterDao: WaterDao
) {
    suspend fun insert(water: Water) = waterDao.insertWater(water)

}