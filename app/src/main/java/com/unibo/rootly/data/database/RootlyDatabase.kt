package com.unibo.rootly.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.unibo.rootly.data.database.daos.BadgeTypeDao
import com.unibo.rootly.data.database.daos.CleaningDao
import com.unibo.rootly.data.database.daos.PetDao
import com.unibo.rootly.data.database.daos.ReceivedDao
import com.unibo.rootly.data.database.daos.PetSpeciesDao
import com.unibo.rootly.data.database.daos.UserDao
import com.unibo.rootly.data.database.daos.FeedingDao
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
    abstract fun fertilizerDao(): CleaningDao
    abstract fun plantDao(): PetDao
    abstract fun receivedDao(): ReceivedDao
    abstract fun speciesDao(): PetSpeciesDao
    abstract fun userDao(): UserDao
    abstract fun waterDao(): FeedingDao

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
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                database.badgeTypeDao().insertAll(InitialData.getBadgeTypes(context))
                                database.speciesDao().insertAll(InitialData.getInitialSpecies())
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