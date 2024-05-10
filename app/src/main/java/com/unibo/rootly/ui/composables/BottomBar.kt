package com.unibo.rootly.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute

@Composable
fun BottomBar(navController: NavHostController, currentRoute: RootlyRoute) {
    NavigationBar {

        //Home
        NavigationBarItem(
            icon = {
                if(currentRoute == RootlyRoute.Home) {
                    Icon(Icons.Filled.Today, contentDescription = "Home")
                }
                else {
                    Icon(Icons.Outlined.Today, contentDescription = "Home")
                }
            },
            label = {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            selected = currentRoute == RootlyRoute.Home,
            onClick = {
                navController.navigate(RootlyRoute.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                }
            }
        )

        //Profile
        NavigationBarItem(
            icon = {
                if(currentRoute == RootlyRoute.UserProfile) {
                    Icon(Icons.Filled.Person, contentDescription = "Profile")
                }
                else {
                    Icon(Icons.Outlined.Person, contentDescription = "Profile")
                }
            },
            label = {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            selected = currentRoute == RootlyRoute.UserProfile,
            onClick = {
                navController.navigate(RootlyRoute.UserProfile.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                }
            }
        )

        //Settings
        NavigationBarItem(
            icon = {
                if(currentRoute == RootlyRoute.Settings) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
                else {
                    Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                }
            },
            label = {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            selected = currentRoute == RootlyRoute.Settings,
            onClick = {
                navController.navigate(RootlyRoute.Settings.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                }
            }
        )
    }
}

