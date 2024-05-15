package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.Plant
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlant(plant: Plant) : Long

    suspend fun insertAllPlants(plants : List<Plant>) = plants.forEach { plant ->  insertPlant(plant)}

    @Query("UPDATE plant SET favorite = 1 WHERE plant_id = :plantId ")
    suspend fun insertLike(plantId : Int)

    @Query("UPDATE plant SET favorite = 0 WHERE plant_id = :plantId ")
    fun removeLike(plantId: Int)

    @Query("UPDATE plant SET dead = 1 WHERE plant_id = :plantId ")
    suspend fun insertDead(plantId : Int)

    @Query("SELECT COUNT(*) FROM Plant WHERE user_id = :userId and dead = 0")
    suspend fun countByUser(userId: Int): Int?

}