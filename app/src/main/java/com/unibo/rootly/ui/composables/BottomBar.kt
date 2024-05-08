package com.unibo.rootly.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute

@Composable
fun BottomBar(navController: NavHostController, route: RootlyRoute) {
    val isHome = route == RootlyRoute.Home

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = isHome,
            onClick = { navController.navigate(RootlyRoute.Home.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = !isHome,
            onClick = { navController.navigate(RootlyRoute.UserProfile.route) }
        )
    }
}

