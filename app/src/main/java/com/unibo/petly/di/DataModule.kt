package com.unibo.petly.di

import android.content.Context
import com.unibo.petly.data.database.PetlyDatabase
import com.unibo.petly.data.database.daos.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideBadgeTypeDao(get()) }
    single { provideFeedingDao(get()) }
    single { providePetDao(get()) }
    single { provideReceivedDao(get()) }
    single { provideSpeciesDao(get()) }
    single { provideUserDao(get()) }
    single { provideCleaningDao(get()) }
}

private fun provideDatabase(context: Context): PetlyDatabase {
    return PetlyDatabase.getDatabase(context)
}

private fun provideBadgeTypeDao(database: PetlyDatabase): BadgeTypeDao {
    return database.badgeTypeDao()
}

private fun provideCleaningDao(database: PetlyDatabase): CleaningDao {
    return database.cleaningDao()
}

private fun providePetDao(database: PetlyDatabase): PetDao {
    return database.petDao()
}

private fun provideReceivedDao(database: PetlyDatabase): ReceivedDao {
    return database.receivedDao()
}

private fun provideSpeciesDao(database: PetlyDatabase): PetSpeciesDao {
    return database.speciesDao()
}

private fun provideUserDao(database: PetlyDatabase): UserDao {
    return database.userDao()
}

private fun provideFeedingDao(database: PetlyDatabase): FeedingDao {
    return database.feedingDao()
}
