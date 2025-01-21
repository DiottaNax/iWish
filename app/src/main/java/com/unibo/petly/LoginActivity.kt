package com.unibo.petly

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.unibo.petly.ui.screens.FormScreen
import com.unibo.petly.ui.theme.RootlyTheme
import com.unibo.petly.viewmodel.SettingsViewModel
import com.unibo.petly.viewmodel.Theme
import org.koin.androidx.compose.koinViewModel

class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val settingsVM = koinViewModel<SettingsViewModel>()
            val sharedPreferences: SharedPreferences = getSharedPreferences("userId", Context.MODE_PRIVATE)

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
                    FormScreen(FormScreen.Login, sharedPreferences, context)
                }
            }
        }
    }
}