package com.unibo.rootly.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.PetlyRoute

sealed class NavBarItem(
    val icon: ImageVector,
    val iconOnSelect: ImageVector,
    val route: PetlyRoute
) {
    data object Home: NavBarItem(
        Icons.Outlined.Checklist,
        Icons.Filled.Checklist,
        PetlyRoute.Home
    )
    data object Profile: NavBarItem(
        Icons.Outlined.Person,
        Icons.Filled.Person, PetlyRoute.UserProfile
    )
    data object Settings: NavBarItem(
        Icons.Outlined.Settings,
        Icons.Filled.Settings,
        PetlyRoute.Settings
    )
}
val navBarItems = listOf(NavBarItem.Home, NavBarItem.Profile, NavBarItem.Settings)

@Composable
fun BottomBar(
    navController: NavHostController,
    currentRoute: PetlyRoute,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        navBarItems.forEach {
            val isSelected = (currentRoute == it.route)
            NavigationBarItem(
                icon = {
                    if (isSelected) {
                        Icon(it.iconOnSelect, it.toString())
                    } else {
                        Icon(it.icon, it.toString())
                    }
                },
                label = {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(it.route.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    }
                },
                modifier = modifier
            )
        }
    }
}

