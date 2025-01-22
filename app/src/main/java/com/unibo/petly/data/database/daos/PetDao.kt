package com.unibo.petly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.petly.data.database.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPet(pet: Pet) : Long

    suspend fun insertAllPets(pets : List<Pet>) = pets.forEach { pet ->  insertPet(pet)}

    @Query("UPDATE pet SET favorite = 1 WHERE pet_id = :petId ")
    suspend fun insertLike(petId : Int)

    @Query("UPDATE pet SET favorite = 0 WHERE pet_id = :petId ")
    fun removeLike(petId: Int)

    @Query("UPDATE pet SET removed = 1 WHERE pet_id = :petId ")
    suspend fun removePet(petId : Int)

    @Query("SELECT COUNT(*) FROM Pet WHERE user_id = :userId and removed = 0")
    suspend fun countByUser(userId: Int): Int?
    @Query("SELECT * FROM Pet WHERE user_id = :userId and removed = 0")
    fun getPetsByUser(userId: Int): Flow<List<Pet>>

}