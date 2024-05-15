package com.unibo.rootly.data.repositories

import android.content.ContentResolver
import android.net.Uri
import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.daos.PlantDao
import com.unibo.rootly.utils.saveImageToStorage

class PlantRepository(
    private val plantDao: PlantDao,
    private val contentResolver: ContentResolver
) {
    @WorkerThread
    suspend fun insert(plant: Plant): Long {
        return if (plant.img?.isNotEmpty() == true) {
            val imageUri = saveImageToStorage(
                Uri.parse(plant.img),
                contentResolver,
                "TravelDiary_Place${plant.plantId}"
            )
            plantDao.insertPlant(plant.copy(img = imageUri.toString()))
        } else {
            plantDao.insertPlant(plant)
        }
    }

    @WorkerThread
    suspend fun insertDead(plantId: Int) = plantDao.insertDead(plantId)

    @WorkerThread
    suspend fun insertLike(plantId: Int) = plantDao.insertLike(plantId)
    @WorkerThread
    suspend fun removeLike(plantId: Int) = plantDao.removeLike(plantId)
    @WorkerThread
    suspend fun countByUser(userId: Int): Int? = plantDao.countByUser(userId)

}