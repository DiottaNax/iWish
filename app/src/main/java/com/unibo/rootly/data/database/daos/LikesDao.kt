package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.unibo.rootly.data.database.Likes

@Dao
interface LikesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLikes(likes: Likes)

}