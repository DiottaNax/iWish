package com.unibo.rootly.di

import android.content.Context
import com.unibo.rootly.data.database.RootlyDatabase
import com.unibo.rootly.data.database.daos.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideBadgeTypeDao(get()) }
    single { provideFertilizerDao(get()) }
    single { providePlantDao(get()) }
    single { provideReceivedDao(get()) }
    single { provideSpeciesDao(get()) }
    single { provideUserDao(get()) }
    single { provideWaterDao(get()) }
}

private fun provideDatabase(context: Context): RootlyDatabase {
    return RootlyDatabase.getDatabase(context)
}

private fun provideBadgeTypeDao(database: RootlyDatabase): BadgeTypeDao {
    return database.badgeTypeDao()
}

private fun provideFertilizerDao(database: RootlyDatabase): FertilizerDao {
    return database.fertilizerDao()
}

private fun providePlantDao(database: RootlyDatabase): PlantDao {
    return database.plantDao()
}

private fun provideReceivedDao(database: RootlyDatabase): ReceivedDao {
    return database.receivedDao()
}

private fun provideSpeciesDao(database: RootlyDatabase): SpeciesDao {
    return database.speciesDao()
}

private fun provideUserDao(database: RootlyDatabase): UserDao {
    return database.userDao()
}

private fun provideWaterDao(database: RootlyDatabase): WaterDao {
    return database.waterDao()
}
