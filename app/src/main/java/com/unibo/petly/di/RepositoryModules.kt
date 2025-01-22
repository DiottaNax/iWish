package com.unibo.petly.di

import android.content.Context
import com.unibo.petly.data.repositories.*
import com.unibo.petly.viewmodel.PetViewModel
import com.unibo.petly.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { PetRepository(
        get(),
        get<Context>().applicationContext.contentResolver
    ) }
    single { ReceivedRepository(get()) }
    single { SpeciesRepository(get()) }
    single {
        UserRepository(
            get(),
            get<Context>().applicationContext.contentResolver
        )
    }
    single { FeedingRepository(get()) }
    single { CleaningRepository(get()) }

    viewModel { UserViewModel() }
    viewModel { PetViewModel() }
}
