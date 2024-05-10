package com.unibo.rootly

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.unibo.rootly.data.repositories.SettingsRepository
import com.unibo.rootly.ui.screens.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    single { get<Context>().dataStore }

    single { SettingsRepository(get()) }

    viewModel { SettingsViewModel(get()) }
}
