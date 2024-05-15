package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Water
import com.unibo.rootly.data.database.daos.WaterDao
import java.time.LocalDate

class WaterRepository(
    private val waterDao: WaterDao
) {
    @WorkerThread
    suspend fun insert(water: Water) = waterDao.insertWater(water)

    @WorkerThread
    fun getDuesBeforeDate(userId: Int, date :LocalDate = LocalDate.now()) = waterDao.getWaterBeforeDate(userId,date)

    @WorkerThread
    fun getAll(userId: Int) = waterDao.getAllWater(userId)

    @WorkerThread
    fun getDuesBeforeDateFavorites(userId: Int, date :LocalDate = LocalDate.now()) = waterDao.getWaterBeforeDateFavorites(userId,date)

    @WorkerThread
    fun getAllFavorites(userId: Int) = waterDao.getAllWaterFavorites(userId)
    @WorkerThread
    suspend fun remove(plantId: Int, date: LocalDate) {
        waterDao.removeWater(plantId,date)
    }

    @WorkerThread
    fun getLastWaterDate(plantId: Int) = waterDao.getLastWateredDate(plantId)

    @WorkerThread
    suspend fun getTimesWatered(userId:Int ) = waterDao.getTimesWatered(userId)
}