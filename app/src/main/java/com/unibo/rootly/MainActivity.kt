package com.unibo.rootly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.unibo.rootly.ui.RootlyNavGraph
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.screens.SettingsViewModel
import com.unibo.rootly.ui.screens.Theme
import com.unibo.rootly.ui.theme.RootlyTheme
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.compose.koinViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                            RootlyRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: RootlyRoute.Registration
                        }
                    }
                    RootlyNavGraph(navController, settingsVM)
                }
            }
        }
    }
}