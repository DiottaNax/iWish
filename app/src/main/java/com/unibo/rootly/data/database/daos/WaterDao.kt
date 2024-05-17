package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.Water
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WaterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWater(water: Water)

    suspend fun insertAllWater(waterList: List<Water>) =
        waterList.forEach { water -> insertWater(water)  }


    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT  plant_id, MAX(date) AS last_watered_date FROM Water GROUP BY plant_id) w " +
            "ON p.plant_id = w.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "WHERE p.user_id = :userId  and p.dead = 0 ")
    fun getAllWater(userId: Int): Flow<List<Plant>>

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT  plant_id, MAX(date) AS last_watered_date FROM Water GROUP BY plant_id) w " +
            "ON p.plant_id = w.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "WHERE p.user_id = :userId  and p.dead = 0 and p.favorite = 1")
    fun getAllWaterFavorites(userId: Int): Flow<List<Plant>>
    @Query("DELETE FROM Water WHERE plant_id = :plantId AND date = :date")
    suspend fun removeWater(plantId: Int, date: LocalDate)

    @Query("SELECT MAX(date) FROM Water WHERE plant_id = :plantId")
    fun getLastWateredDate(plantId: Int) :LocalDate?

    @Query("SELECT COUNT(*) " +
            "FROM Water w join Plant p " +
            "on p.plant_id = w.plant_id " +
            "WHERE p.user_id =:userId")
    suspend fun getTimesWatered(userId :Int) :Int
}
