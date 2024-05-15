package com.unibo.rootly.di

import com.unibo.rootly.data.repositories.*
import com.unibo.rootly.viewmodel.PlantViewModel
import com.unibo.rootly.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { PlantRepository(get()) }
    single { ReceivedRepository(get()) }
    single { SpeciesRepository(get()) }
    single { UserRepository(get()) }
    single { WaterRepository(get()) }
    single { FertilizerRepository(get()) }

    viewModel { UserViewModel() }
    viewModel { PlantViewModel() }
}
