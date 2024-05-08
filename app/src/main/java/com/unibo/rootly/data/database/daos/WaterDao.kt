package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.unibo.rootly.data.database.Water
@Dao
interface WaterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWater(water: Water)
}
