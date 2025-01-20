package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.Cleaning
import com.unibo.rootly.data.database.Pet
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CleaningDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCleaning(cleaning: Cleaning)

    suspend fun insertAllCleaning(cleanings: List<Cleaning>) =
        cleanings.forEach { cleaning -> insertCleaning(cleaning)  }

    @Query("SELECT p.* " +
            "FROM Pet p " +
            "left outer JOIN (SELECT  pet_id, MAX(date) AS last_fert_date FROM Cleaning GROUP BY pet_id) f " +
            "ON p.pet_id = f.pet_id " +
            "JOIN Pet_Species s ON s.specie_name = p.specie " +
            "WHERE p.user_id = :userId and p.removed = 0 ")
    fun getAllCleaning(userId: Int): Flow<List<Pet>>

    @Query("SELECT p.* " +
            "FROM Pet p " +
            "left outer JOIN (SELECT  pet_id, MAX(date) AS last_fert_date FROM Cleaning GROUP BY pet_id) f " +
            "ON p.pet_id = f.pet_id " +
            "JOIN Pet_Species s ON s.specie_name = p.specie " +
            "WHERE p.user_id = :userId and p.removed = 0 and p.favorite = 1 ")
    fun getAllCleaningFavorites(userId: Int): Flow<List<Pet>>

    @Query("DELETE FROM Cleaning WHERE pet_id = :plantId AND date = :date")
    suspend fun removeFertilize(plantId: Int, date: LocalDate)

    @Query("SELECT MAX(date) FROM Cleaning WHERE pet_id = :plantId")
    fun getLastCleaningDate(plantId: Int) :LocalDate?

    @Query("SELECT COUNT(*) " +
            "FROM Cleaning f join Pet p " +
            "on p.pet_id = f.pet_id " +
            "WHERE p.user_id =:userId")
    fun getCleaningTimes(userId: Int): Int
}