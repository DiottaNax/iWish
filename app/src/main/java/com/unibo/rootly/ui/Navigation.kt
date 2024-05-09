package com.unibo.rootly.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.unibo.rootly.ui.screens.AddPlantScreen
import com.unibo.rootly.ui.screens.HomeScreen
import com.unibo.rootly.ui.screens.LoginScreen
import com.unibo.rootly.ui.screens.PlantDetailsScreen
import com.unibo.rootly.ui.screens.RegistrationScreen
import com.unibo.rootly.ui.screens.SettingsScreen
import com.unibo.rootly.ui.screens.SettingsViewModel
import com.unibo.rootly.ui.screens.UserProfileScreen
import org.koin.androidx.compose.koinViewModel

sealed class RootlyRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
){
    data object Registration : RootlyRoute("registration", "Welcome to rootly!")
    data object Login : RootlyRoute("login", "Nice to see you again!")
    data object Home : RootlyRoute("plants" , "Today's to-do")
    data object Settings : RootlyRoute("profile/settings", "Settings")
    data object AddPlant : RootlyRoute("plants/add","Add a new plant")
    data object PlantDetails: RootlyRoute(
        "plants/{plantId}",
        "{plantName}",
        listOf(navArgument("plantId") { type = NavType.StringType },
            navArgument("plantName") { type = NavType.StringType })
    ){
        fun buildRoute(plantId: String) = "plants/$plantId"
    }

    data object UserProfile : RootlyRoute(
        "user/{userId}",
        "{username}",
        listOf(navArgument("userId") { type = NavType.StringType },
            navArgument("username") { type = NavType.StringType })
    ){
        fun buildRoute(userId: String) = "User/$userId" // TODO route?
    }

    companion object {
        val routes = setOf(Login, Registration, Home, Settings, PlantDetails, UserProfile, AddPlant)
    }
}

@Composable
fun RootlyNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = RootlyRoute.Registration.route,
        modifier = modifier
    ) {
        with(RootlyRoute.Registration) {
            composable(route) {
                RegistrationScreen(navController)
            }
        }
        with(RootlyRoute.Login) {
            composable(route) {
                LoginScreen(navController)
            }
        }
        with(RootlyRoute.Home) {
            composable(route) {
                HomeScreen(navController)
            }
        }
        with(RootlyRoute.UserProfile) {
            composable(route) { backStackEntry ->
                UserProfileScreen(navController, backStackEntry.arguments?.getString("userId") ?: "")
            }
        }
        with(RootlyRoute.PlantDetails) {
            composable(route) { backStackEntry ->
                PlantDetailsScreen(navController, backStackEntry.arguments?.getString("plantId") ?: "")
            }
        }
        with(RootlyRoute.AddPlant) {
            composable(route) {
                AddPlantScreen(navController =  navController)
            }
        }
        with(RootlyRoute.Settings) {
            composable(route) {
                val settingsVm = koinViewModel<SettingsViewModel>()
                SettingsScreen(settingsVm.state, settingsVm::setUsername)
            }
        }
    }
}