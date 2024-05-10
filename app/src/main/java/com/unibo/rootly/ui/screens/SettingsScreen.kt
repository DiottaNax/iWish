package com.unibo.rootly.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.BottomBar
import com.unibo.rootly.ui.composables.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    vm: SettingsViewModel,
    state: ThemeState
) {
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                currentRoute = RootlyRoute.Settings
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = RootlyRoute.Settings
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp, 0.dp)
        ) {
            Text(
                text = "Select your theme:",
                style = MaterialTheme.typography.bodyMedium)
            Theme.entries.forEach { theme ->
                val selected = (theme == state.theme)

                FilterChip(
                    onClick = { vm.changeTheme(theme) },
                    label = {
                        Text(theme.toString())
                    },
                    selected = selected,
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Choosen icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
            }
        }
    }
}