package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.PlantLog
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantLogDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlantLog(plantLog: PlantLog)

    @Query("SELECT * FROM Plant_log WHERE user_id = :userId AND plant_id = :plantId")
    suspend fun getPlantLogsByPlant(userId: Int, plantId: Int): Flow<List<PlantLog>>

}