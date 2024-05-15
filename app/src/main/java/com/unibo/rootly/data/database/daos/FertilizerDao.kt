package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.Fertilizer
import com.unibo.rootly.data.database.Plant
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface FertilizerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFertilizer(fertilizer: Fertilizer)

    suspend fun insertAllFertilizer(fertilizers: List<Fertilizer>) =
        fertilizers.forEach { fertilizer -> insertFertilizer(fertilizer)  }

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT  plant_id, MAX(date) AS last_fert_date FROM Fertilizer GROUP BY plant_id) f " +
            "ON p.plant_id = f.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "WHERE p.user_id = :userId and p.dead = 0 " +
            "AND s.water_frequency <= CAST((julianday(:today) - julianday(f.last_fert_date)) AS INTEGER)")
    fun getFertilizeBeforeDate(userId: Int, today: LocalDate): Flow<List<Plant>>


    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT  plant_id, MAX(date) AS last_fert_date FROM Fertilizer GROUP BY plant_id) f " +
            "ON p.plant_id = f.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "WHERE p.user_id = :userId and p.dead = 0 ")
    fun getAllFertilize(userId: Int): Flow<List<Plant>>

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT  plant_id, MAX(date) AS last_fert_date FROM Fertilizer GROUP BY plant_id) f " +
            "ON p.plant_id = f.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "WHERE p.user_id = :userId and p.dead = 0 and p.favorite = 1 " +
            "AND s.water_frequency <= CAST((julianday(:today) - julianday(f.last_fert_date)) AS INTEGER)")
    fun getFertilizeBeforeDateFavorites(userId: Int, today: LocalDate): Flow<List<Plant>>


    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT  plant_id, MAX(date) AS last_fert_date FROM Fertilizer GROUP BY plant_id) f " +
            "ON p.plant_id = f.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "WHERE p.user_id = :userId and p.dead = 0 and p.favorite = 1 ")
    fun getAllFertilizeFavorites(userId: Int): Flow<List<Plant>>

    @Query("DELETE FROM Fertilizer WHERE plant_id = :plantId AND date = :date")
    suspend fun removeFertilize(plantId: Int, date: LocalDate)

    @Query("SELECT MAX(date) FROM Fertilizer WHERE plant_id = :plantId")
    fun getLastFertilizeDate(plantId: Int) :LocalDate?

    @Query("SELECT COUNT(*) " +
            "FROM Fertilizer f join Plant p " +
            "on p.plant_id = f.plant_id " +
            "WHERE p.user_id =:userId")
    fun getTimesFertilized(userId: Int): Int
}