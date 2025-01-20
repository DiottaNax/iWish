package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Water
import com.unibo.rootly.data.database.daos.WaterDao
import java.time.LocalDate

class WaterRepository(
    private val waterDao: WaterDao
) {
    suspend fun insert(water: Water) = waterDao.insertWater(water)

    fun getAll(userId: Int) = waterDao.getAllWater(userId)

    fun getAllFavorites(userId: Int) = waterDao.getAllWaterFavorites(userId)

    suspend fun remove(plantId: Int, date: LocalDate) {
        waterDao.removeWater(plantId,date)
    }

    fun getLastWaterDate(plantId: Int) = waterDao.getLastWateredDate(plantId)

    suspend fun getTimesWatered(userId:Int ) = waterDao.getTimesWatered(userId)
}