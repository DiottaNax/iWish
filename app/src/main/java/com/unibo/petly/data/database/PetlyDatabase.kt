package com.unibo.petly.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.unibo.petly.data.database.daos.BadgeTypeDao
import com.unibo.petly.data.database.daos.CleaningDao
import com.unibo.petly.data.database.daos.PetDao
import com.unibo.petly.data.database.daos.ReceivedDao
import com.unibo.petly.data.database.daos.PetSpeciesDao
import com.unibo.petly.data.database.daos.UserDao
import com.unibo.petly.data.database.daos.FeedingDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Database(
    entities = [
        BadgeType::class,
        Feeding::class,
        Pet::class,
        Received::class,
        PetSpecies::class,
        User::class,
        Cleaning::class
    ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class PetlyDatabase : RoomDatabase() {

    abstract fun badgeTypeDao(): BadgeTypeDao
    abstract fun cleaningDao(): CleaningDao
    abstract fun petDao(): PetDao
    abstract fun receivedDao(): ReceivedDao
    abstract fun speciesDao(): PetSpeciesDao
    abstract fun userDao(): UserDao
    abstract fun feedingDao(): FeedingDao

    companion object {
        @Volatile
        private var INSTANCE: PetlyDatabase? = null

        fun getDatabase(context: Context): PetlyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PetlyDatabase::class.java,
                    "petly_database"
                ).allowMainThreadQueries()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                database.badgeTypeDao().insertAll(InitialData.getBadgeTypes(context))
                                database.speciesDao().insertAll(InitialData.getInitialPetSpecies())
                                InitialData.getInitialUsers().forEach {database.userDao().insertUser(it)}
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