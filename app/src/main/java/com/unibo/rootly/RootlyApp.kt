package com.unibo.rootly

import android.app.Application
import com.unibo.rootly.data.database.RootlyDatabase
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@HiltAndroidApp
class RootlyApp : Application() {
    val database by lazy { RootlyDatabase.getDatabase(this) }
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RootlyApp)
            modules(appModule)
        }
    }
}