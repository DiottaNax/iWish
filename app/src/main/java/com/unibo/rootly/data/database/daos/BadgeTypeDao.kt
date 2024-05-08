package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.unibo.rootly.data.database.BadgeType
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeTypeDao {
    @Query("SELECT * FROM Badge_Type")
    suspend fun getAllBadgeTypes(): Flow<List<BadgeType>>
}