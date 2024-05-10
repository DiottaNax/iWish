package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.BadgeType
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeTypeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBadge(badge: BadgeType)
    @Query("SELECT * FROM Badge_Type")
    fun getAllBadgeTypes(): Flow<List<BadgeType>>
    suspend fun insertAll( badgeTypes: List<BadgeType>) = badgeTypes.forEach{ b -> insertBadge(b)}


}