package com.unibo.petly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.petly.data.database.Pet
import com.unibo.petly.data.database.Feeding
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface FeedingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFeeding(feeding: Feeding)

    suspend fun insertAllFeeding(waterList: List<Feeding>) =
        waterList.forEach { water -> insertFeeding(water)  }


    @Query("SELECT p.* " +
            "FROM Pet p " +
            "left outer JOIN (SELECT  pet_id, MAX(date) AS last_feeding_date FROM Feeding GROUP BY pet_id) w " +
            "ON p.pet_id = w.pet_id " +
            "JOIN Pet_Species s ON s.specie_name = p.specie " +
            "WHERE p.user_id = :userId  and p.removed = 0 ")
    fun getAllFeeding(userId: Int): Flow<List<Pet>>

    @Query("SELECT p.* " +
            "FROM Pet p " +
            "left outer JOIN (SELECT  pet_id, MAX(date) AS last_watered_date FROM Feeding GROUP BY pet_id) w " +
            "ON p.pet_id = w.pet_id " +
            "JOIN Pet_Species s ON s.specie_name = p.specie " +
            "WHERE p.user_id = :userId  and p.removed = 0 and p.favorite = 1")
    fun getAllFeedingFavorites(userId: Int): Flow<List<Pet>>
    @Query("DELETE FROM Feeding WHERE pet_id = :petId AND date = :date")
    suspend fun removeFeeding(petId: Int, date: LocalDate)

    @Query("SELECT MAX(date) FROM Feeding WHERE pet_id = :petId")
    fun getLastFeedingDate(petId: Int) :LocalDate?

    @Query("SELECT COUNT(*) " +
            "FROM Feeding w join Pet p " +
            "on p.pet_id = w.pet_id " +
            "WHERE p.user_id =:userId")
    suspend fun getFeedingTimes(userId :Int) :Int
}
