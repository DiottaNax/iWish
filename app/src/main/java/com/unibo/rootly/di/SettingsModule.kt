package com.unibo.rootly.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.unibo.rootly.data.repositories.SettingsRepository
import com.unibo.rootly.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val settingsModule = module {
    single { get<Context>().dataStore }

    single { SettingsRepository(get()) }

    viewModel { SettingsViewModel(get()) }
}
