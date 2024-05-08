package com.unibo.rootly.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unibo.rootly.data.database.daos.BadgeTypeDao
import com.unibo.rootly.data.database.daos.FertilizerDao
import com.unibo.rootly.data.database.daos.LikesDao
import com.unibo.rootly.data.database.daos.PlantDao
import com.unibo.rootly.data.database.daos.PlantLogDao
import com.unibo.rootly.data.database.daos.ReceivedDao
import com.unibo.rootly.data.database.daos.SpeciesDao
import com.unibo.rootly.data.database.daos.UserDao
import com.unibo.rootly.data.database.daos.WaterDao

@Database(
    entities = [
        BadgeType::class,
        Fertilizer::class,
        Likes::class,
        Plant::class,
        Received::class,
        Species::class,
        PlantLog::class,
        User::class,
        Water::class
    ],
    version = 1
)
abstract class RootlyDatabase : RoomDatabase() {

    abstract fun badgeTypeDao(): BadgeTypeDao
    abstract fun fertilizerDao(): FertilizerDao
    abstract fun likesDao(): LikesDao
    abstract fun plantDao(): PlantDao
    abstract fun receivedDao(): ReceivedDao
    abstract fun speciesDao(): SpeciesDao
    abstract fun plantLogDao(): PlantLogDao
    abstract fun userDao(): UserDao
    abstract fun waterDao(): WaterDao

    companion object {
        @Volatile
        private var INSTANCE: RootlyDatabase? = null

        fun getDatabase(context: Context): RootlyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RootlyDatabase::class.java,
                    "your_database_name"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
