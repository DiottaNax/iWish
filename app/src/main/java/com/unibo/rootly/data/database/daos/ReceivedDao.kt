package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.BadgeType
import com.unibo.rootly.data.database.Received
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceivedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReceived(received: Received)

    @Query("SELECT b.* FROM Received r join Badge_Type b on r.name = b.name WHERE r.user_id = :userId")
    fun getReceivedByUser(userId: Int): Flow<List<BadgeType>>
}