package com.unibo.rootly

import android.app.Application
import com.unibo.rootly.data.database.RootlyDatabase
import com.unibo.rootly.di.databaseModule
import com.unibo.rootly.di.repositoryModule
import com.unibo.rootly.di.settingsModule
import com.unibo.rootly.utils.Notifications
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RootlyApp : Application() {
    val database by lazy { RootlyDatabase.getDatabase(this) }
    override fun onCreate() {
        super.onCreate()
        Notifications.initialize(this)
        startKoin {
            androidLogger()
            androidContext(this@RootlyApp)
            modules(settingsModule, databaseModule , repositoryModule)
        }
    }
}