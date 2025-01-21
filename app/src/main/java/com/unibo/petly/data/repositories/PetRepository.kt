package com.unibo.petly.data.repositories

import android.content.ContentResolver
import android.net.Uri
import com.unibo.petly.data.database.Pet
import com.unibo.petly.data.database.daos.PetDao
import com.unibo.petly.utils.saveImageToStorage

class PetRepository(
    private val petDao: PetDao,
    private val contentResolver: ContentResolver
) {
    suspend fun insert(pet: Pet): Long {
        return if (pet.img?.isNotEmpty() == true) {
            val imageUri = saveImageToStorage(
                Uri.parse(pet.img),
                contentResolver,
                "PetImage${pet.petId}"
            )
            petDao.insertPet(pet.copy(img = imageUri.toString()))
        } else {
            petDao.insertPet(pet)
        }
    }

    suspend fun removePet(petId: Int) = petDao.removePet(petId)

    suspend fun insertLike(petId: Int) = petDao.insertLike(petId)

    fun removeLike(petId: Int) = petDao.removeLike(petId)

    suspend fun countByUser(userId: Int): Int? = petDao.countByUser(userId)
    fun getPetsByUser(userId: Int) = petDao.getPetsByUser(userId)

}