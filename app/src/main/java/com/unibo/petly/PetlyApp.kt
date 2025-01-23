package com.unibo.petly

import android.app.Application
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.unibo.petly.data.database.PetlyDatabase
import com.unibo.petly.di.databaseModule
import com.unibo.petly.di.repositoryModule
import com.unibo.petly.di.settingsModule
import com.unibo.petly.utils.Notifications
import com.unibo.petly.utils.UserCheckWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class PetlyApp : Application() {
    val database by lazy { PetlyDatabase.getDatabase(this) }
    override fun onCreate() {
        super.onCreate()
        Notifications.initialize(this)
        startKoin {
            androidLogger()
            androidContext(this@PetlyApp)
            modules(settingsModule, databaseModule , repositoryModule)
        }

        // add a check everyday to check badges about app using
        val userCheckWorkRequest = PeriodicWorkRequest.Builder(
            UserCheckWorker::class.java,
            1,
            TimeUnit.DAYS
        ).setInitialDelay(1, TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext).enqueue(userCheckWorkRequest)
    }
}