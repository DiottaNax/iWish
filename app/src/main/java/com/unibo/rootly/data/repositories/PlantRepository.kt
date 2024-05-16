package com.unibo.rootly.data.repositories

import android.content.ContentResolver
import android.net.Uri
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.daos.PlantDao
import com.unibo.rootly.utils.saveImageToStorage

class PlantRepository(
    private val plantDao: PlantDao,
    private val contentResolver: ContentResolver
) {
    suspend fun insert(plant: Plant): Long {
        return if (plant.img?.isNotEmpty() == true) {
            val imageUri = saveImageToStorage(
                Uri.parse(plant.img),
                contentResolver,
                "PlantImage${plant.plantId}"
            )
            plantDao.insertPlant(plant.copy(img = imageUri.toString()))
        } else {
            plantDao.insertPlant(plant)
        }
    }

    suspend fun insertDead(plantId: Int) = plantDao.insertDead(plantId)

    suspend fun insertLike(plantId: Int) = plantDao.insertLike(plantId)

    fun removeLike(plantId: Int) = plantDao.removeLike(plantId)

    suspend fun countByUser(userId: Int): Int? = plantDao.countByUser(userId)

}