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

    @Query("SELECT * FROM Plant_log WHERE plant_id = :plantId")
    fun getPlantLogsByPlant(plantId: Int): Flow<List<PlantLog>>

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT  plant_id, MAX(date) AS last_log_date FROM Plant_log GROUP by plant_id) l " +
            "ON p.plant_id = l.plant_id " +
            "WHERE p.user_id = :userId AND 15 <= " +
            "   Cast ((julianday('now') - julianday(l.last_log_date)) As Integer)")
    fun getTodayLogs(userId: Int): Flow<List<Plant>>

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT plant_id, MAX(date) AS last_log_date FROM Plant_log GROUP BY plant_id) l " +
            "ON p.plant_id = l.plant_id " +
            "WHERE p.user_id = :userId AND 14 >" +
            "   Cast ((julianday('now') - julianday(l.last_log_date)) As Integer)" +
            "AND 12 <= Cast ((julianday('now') - julianday(l.last_log_date)) As Integer)")
    fun getSoonLogs(userId: Int): Flow<List<Plant>>

}