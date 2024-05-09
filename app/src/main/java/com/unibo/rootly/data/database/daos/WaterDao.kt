package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.Water
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWater(water: Water)

    suspend fun insertAllWater(waterList: List<Water>) =
        waterList.forEach { water -> insertWater(water)  }

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT user_id, plant_id, MAX(date) AS last_watered_date FROM Water GROUP BY user_id, plant_id) w " +
            "ON p.user_id = w.user_id AND p.plant_id = w.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "WHERE p.user_id = :userId AND s.water_frequency <=" +
            "   Cast ((julianday('now') - julianday(w.last_watered_date)) As Integer)")
    fun getTodayWater(userId: Int): Flow<List<Plant>>

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT user_id, plant_id, MAX(date) AS last_watered_date FROM Water GROUP BY user_id, plant_id) w " +
            "ON p.user_id = w.user_id AND p.plant_id = w.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "WHERE p.user_id = :userId " +
            "AND s.water_frequency > " +
            "   Cast ((julianday('now') - julianday(w.last_watered_date)) As Integer)" +
            "AND s.water_frequency <=" +
            "   Cast ((julianday('now') - julianday(w.last_watered_date)) As Integer) + 2")
    fun getSoonWater(userId: Int): Flow<List<Plant>>

}
