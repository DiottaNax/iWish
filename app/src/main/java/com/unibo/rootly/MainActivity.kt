package com.unibo.rootly

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unibo.rootly.ui.RootlyNavGraph
import com.unibo.rootly.ui.PetlyRoute
import com.unibo.rootly.ui.composables.TopBar
import com.unibo.rootly.ui.theme.RootlyTheme
import com.unibo.rootly.utils.LocationService
import com.unibo.rootly.viewmodel.SettingsViewModel
import com.unibo.rootly.viewmodel.Theme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = getSharedPreferences("userId", Context.MODE_PRIVATE)
        locationService = LocationService(this)

        setContent {
            val context = LocalContext.current
            val settingsVM = koinViewModel<SettingsViewModel>()


            RootlyTheme(
                darkTheme = when (settingsVM.state.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            PetlyRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: PetlyRoute.Home
                        }
                    }

                    val userId = sharedPreferences.getInt("userId",-1)
                    if ( userId > 0) {
                        Scaffold(
                            topBar = { TopBar(
                                navController = navController,
                                currentRoute = currentRoute,
                                sharedPreferences = sharedPreferences,
                                context = context
                            )}
                        ) { innerPadding ->
                            RootlyNavGraph(
                                navController = navController,
                                settingsVM = settingsVM,
                                locationService = locationService,
                                userId = userId,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    } else {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        (context as Activity).finish()
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        locationService.resumeLocationRequest()
    }
}