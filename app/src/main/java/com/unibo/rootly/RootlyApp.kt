package com.unibo.rootly

import android.app.Application
import com.unibo.rootly.data.database.RootlyDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RootlyApp : Application() {
    val database by lazy { RootlyDatabase.getDatabase(this) }
}