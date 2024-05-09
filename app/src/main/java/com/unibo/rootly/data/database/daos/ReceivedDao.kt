package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.Received

@Dao
interface ReceivedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReceived(received: Received)

    @Query("SELECT * FROM Received WHERE user_id = :userId")
    suspend fun getReceivedByUser(userId: Int): List<Received>
}