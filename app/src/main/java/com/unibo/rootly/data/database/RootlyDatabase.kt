package com.unibo.rootly.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.unibo.rootly.data.database.daos.BadgeTypeDao
import com.unibo.rootly.data.database.daos.FertilizerDao
import com.unibo.rootly.data.database.daos.PlantDao
import com.unibo.rootly.data.database.daos.ReceivedDao
import com.unibo.rootly.data.database.daos.SpeciesDao
import com.unibo.rootly.data.database.daos.UserDao
import com.unibo.rootly.data.database.daos.WaterDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Database(
    entities = [
        BadgeType::class,
        Fertilizer::class,
        Plant::class,
        Received::class,
        Species::class,
        User::class,
        Water::class
    ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class RootlyDatabase : RoomDatabase() {

    abstract fun badgeTypeDao(): BadgeTypeDao
    abstract fun fertilizerDao(): FertilizerDao
    abstract fun plantDao(): PlantDao
    abstract fun receivedDao(): ReceivedDao
    abstract fun speciesDao(): SpeciesDao
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
                    "rootly_database"
                ).allowMainThreadQueries()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            //todo not working
                            CoroutineScope(Dispatchers.IO).launch {
                                database.badgeTypeDao().insertAll(InitialData.getInitialBadgeTypes())
                                database.speciesDao().insertAll(InitialData.getInitialSpecies())
                                database.userDao().insertUser(InitialData.getInitialUser())
                                database.plantDao().insertAllPlants(InitialData.getInitialPlants())
                                database.waterDao().insertAllWater(InitialData.getInitialWaters())
                                database.fertilizerDao().insertAllFertilizer(InitialData.getInitialFertilizers())
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.toString()
    }
}