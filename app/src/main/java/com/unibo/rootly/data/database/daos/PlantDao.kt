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
    suspend fun insertPlant(plant: Plant)

    suspend fun insertAllPlants(plants : List<Plant>) = plants.forEach { plant ->  insertPlant(plant)}

    @Query("SELECT * FROM Plant WHERE user_id = :userId")
    fun getPlantsByUser(userId: Int): Flow<List<Plant>>

}