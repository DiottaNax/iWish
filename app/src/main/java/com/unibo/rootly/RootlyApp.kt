package com.unibo.rootly

import android.app.Application
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.unibo.rootly.data.database.RootlyDatabase
import com.unibo.rootly.di.databaseModule
import com.unibo.rootly.di.repositoryModule
import com.unibo.rootly.di.settingsModule
import com.unibo.rootly.utils.Notifications
import com.unibo.rootly.utils.PlantCheckWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

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

        // add a check everyday to check badges about plantLife
        val plantCheckWorkRequest = PeriodicWorkRequest.Builder(
            PlantCheckWorker::class.java,
            1,
            TimeUnit.DAYS
        ).setInitialDelay(30, TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext).enqueue(plantCheckWorkRequest)
    }
}