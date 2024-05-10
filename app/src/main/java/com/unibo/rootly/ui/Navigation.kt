package com.unibo.rootly.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.unibo.rootly.ui.screens.ThemeState
import com.unibo.rootly.ui.screens.UserProfileScreen
import com.unibo.rootly.viewmodel.FertilizerViewModel
import com.unibo.rootly.viewmodel.LikesViewModel
import com.unibo.rootly.viewmodel.PlantViewModel
import com.unibo.rootly.viewmodel.ReceivedViewModel
import com.unibo.rootly.viewmodel.SpeciesViewModel
import com.unibo.rootly.viewmodel.UserViewModel
import com.unibo.rootly.viewmodel.WaterViewModel

sealed class RootlyRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
){
    data object Registration : RootlyRoute("registration", "Welcome to rootly")
    data object Login : RootlyRoute("login", "Nice to see you again!")
    data object Home : RootlyRoute("plants" , "To-do")
    data object Settings : RootlyRoute("settings", "Settings")
    data object AddPlant : RootlyRoute("add","Add a plant")
    data object PlantDetails: RootlyRoute(
        "plants/{plantId}/{plantName}", // Include plantName in the route
        "{plantName}",
        listOf(
            navArgument("plantId") { type = NavType.StringType },
            navArgument("plantName") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(plantId: String, plantName: String) = "plants/$plantId/$plantName"
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
    settingsVM: SettingsViewModel,
    state: ThemeState,
    modifier: Modifier = Modifier
) {
    val  fertilizerViewModel = hiltViewModel<FertilizerViewModel>()
    val  likesViewModel = hiltViewModel<LikesViewModel>()
    val  plantViewModel = hiltViewModel<PlantViewModel>()
    val  receivedViewModel= hiltViewModel<ReceivedViewModel>()
    val  speciesViewModel= hiltViewModel<SpeciesViewModel>()
    val  userViewModel = hiltViewModel<UserViewModel>()
    val  waterViewModel = hiltViewModel<WaterViewModel>()

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
                HomeScreen(
                    navController,
                    waterViewModel,
                    fertilizerViewModel,
                    plantViewModel
                )
            }
        }
        with(RootlyRoute.UserProfile) {
            composable(route) { backStackEntry ->
                UserProfileScreen(navController,
                    receivedViewModel,
                    backStackEntry.arguments?.getString("userId") ?: "")
            }
        }
        with(RootlyRoute.PlantDetails) {
            composable(route) { backStackEntry ->
                PlantDetailsScreen(navController,
                    plantViewModel,
                    backStackEntry.arguments?.getString("plantId") ?: "")
            }
        }
        with(RootlyRoute.AddPlant) {
            composable(route) {
                AddPlantScreen(navController =  navController,plantViewModel,)
            }
        }
        with(RootlyRoute.Settings) {
            composable(route) {
                SettingsScreen(navController, settingsVM, state)
            }
        }
    }
}