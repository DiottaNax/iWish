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
            "AND s.fertilizer_frequency <= Cast ((julianday('now') - julianday(f.last_fert_date)) As Integer)")
    fun getTodayFertilizer(userId: Int): Flow<List<Plant>>

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT plant_id, MAX(date) AS last_fert_date FROM Fertilizer GROUP BY plant_id) f " +
            "ON p.plant_id = f.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "WHERE p.user_id = :userId and p.dead = 0" +
            " AND s.fertilizer_frequency >" +
            "    Cast ((julianday('now') - julianday(f.last_fert_date)) As Integer)" +
            "AND s.fertilizer_frequency <= " +
            "   Cast ((julianday('now') - julianday(f.last_fert_date)) As Integer) +2")
    fun getSoonFertilizer(userId: Int): Flow<List<Plant>>

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT  plant_id, MAX(date) AS last_fert_date FROM Fertilizer GROUP BY plant_id) f " +
            "ON p.plant_id = f.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "JOIN Likes l ON l.plant_id = p.plant_id " +
            "WHERE p.user_id = :userId and p.dead = 0 " +
            "AND s.fertilizer_frequency <= Cast ((julianday('now') - julianday(f.last_fert_date)) As Integer)")
    fun getFavoriteTodayFertilizer(userId: Int): Flow<List<Plant>>

    @Query("SELECT p.* " +
            "FROM Plant p " +
            "left outer JOIN (SELECT plant_id, MAX(date) AS last_fert_date FROM Fertilizer GROUP BY plant_id) f " +
            "ON p.plant_id = f.plant_id " +
            "JOIN Specie s ON s.scientific_name = p.scientific_name " +
            "JOIN Likes l ON l.plant_id = p.plant_id " +
            "WHERE p.user_id = :userId and p.dead = 0" +
            " AND s.fertilizer_frequency >" +
            "    Cast ((julianday('now') - julianday(f.last_fert_date)) As Integer)" +
            "AND s.fertilizer_frequency <= " +
            "   Cast ((julianday('now') - julianday(f.last_fert_date)) As Integer) +2")
    fun getFavoriteSoonFertilizer(userId: Int): Flow<List<Plant>>
    @Query("DELETE FROM Fertilizer WHERE plant_id = :plantId AND date = :date")

    suspend fun removeFertilize(plantId: Int, date: LocalDate)
}