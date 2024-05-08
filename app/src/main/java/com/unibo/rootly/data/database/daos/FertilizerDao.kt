package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.unibo.rootly.data.database.Fertilizer

@Dao
interface FertilizerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFertilizer(fertilizer: Fertilizer)
}