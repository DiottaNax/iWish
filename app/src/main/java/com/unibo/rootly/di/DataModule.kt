package com.unibo.rootly.di

import android.content.Context
import androidx.room.Room
import com.unibo.rootly.data.database.RootlyDatabase
import com.unibo.rootly.data.database.daos.BadgeTypeDao
import com.unibo.rootly.data.database.daos.FertilizerDao
import com.unibo.rootly.data.database.daos.PlantDao
import com.unibo.rootly.data.database.daos.ReceivedDao
import com.unibo.rootly.data.database.daos.SpeciesDao
import com.unibo.rootly.data.database.daos.UserDao
import com.unibo.rootly.data.database.daos.WaterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RootlyDatabase {
        return Room.databaseBuilder(context, RootlyDatabase::class.java, "rootly_database").build()
    }

    @Provides
    fun provideBadgeTypeDao(database: RootlyDatabase): BadgeTypeDao {
        return database.badgeTypeDao()
    }

    @Provides
    fun provideFertilizerDao(database: RootlyDatabase): FertilizerDao {
        return database.fertilizerDao()
    }

    @Provides
    fun providePlantDao(database: RootlyDatabase): PlantDao {
        return database.plantDao()
    }
    @Provides
    fun provideReceivedDao(database: RootlyDatabase): ReceivedDao {
        return database.receivedDao()
    }
    @Provides
    fun provideSpeciesDao(database: RootlyDatabase): SpeciesDao {
        return database.speciesDao()
    }
    @Provides
    fun provideUserDao(database: RootlyDatabase): UserDao {
        return database.userDao()
    }
    @Provides
    fun provideWaterDao(database: RootlyDatabase): WaterDao {
        return database.waterDao()
    }
}