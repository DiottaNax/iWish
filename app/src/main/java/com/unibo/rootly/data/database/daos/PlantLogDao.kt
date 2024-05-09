package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.PlantLog
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantLogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlantLog(plantLog: PlantLog)
    suspend fun insertAllPlantLogs(logs : List<PlantLog>) = logs.forEach { log ->  insertPlantLog(log)}

    @Query("SELECT * FROM Plant_log WHERE user_id = :userId AND plant_id = :plantId")
    suspend fun getPlantLogsByPlant(userId: Int, plantId: Int): Flow<List<PlantLog>>

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "JOIN (SELECT user_id, plant_id, MAX(date) AS last_log_date FROM Plant_log GROUP BY user_id, plant_id) l " +
            "ON p.user_id = l.user_id AND p.plant_id = l.plant_id " +
            "WHERE p.user_id = :userId AND 15 <= DATEDIFF('now', l.last_log_date)")
    fun getTodayLogs(userId: Int): Flow<List<Plant>>

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "JOIN (SELECT user_id, plant_id, MAX(date) AS last_log_date FROM Plant_log GROUP BY user_id, plant_id) l " +
            "ON p.user_id = l.user_id AND p.plant_id = l.plant_id " +
            "WHERE p.user_id = :userId AND 14 > DATEDIFF('now', l.last_log_date)" +
            "AND 12 <= DATEDIFF('now', l.last_log_date)")
    fun getSoonLogs(userId: Int): Flow<List<Plant>>

}