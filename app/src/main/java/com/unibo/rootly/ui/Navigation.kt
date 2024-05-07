package com.unibo.rootly.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.unibo.rootly.ui.screens.addplant.AddPlantScreen
import com.unibo.rootly.ui.screens.explore.ExploreScreen
import com.unibo.rootly.ui.screens.home.HomeScreen
import com.unibo.rootly.ui.screens.plantdetails.PlantDetailsScreen
import com.unibo.rootly.ui.screens.settings.SettingsScreen
import com.unibo.rootly.ui.screens.settings.SettingsViewModel
import com.unibo.rootly.ui.screens.userdetails.UserDetailsScreen
import org.koin.androidx.compose.koinViewModel

sealed class RootlyRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
){
    data object Home : RootlyRoute("home" , "Home") // TODO route?
    data object Explore : RootlyRoute("explore" , "All Plants") //TODO route?
    data object Settings : RootlyRoute("settings", "Settings")
    data object AddPlant : RootlyRoute("plants/addPlant","Add Plant")
    data object PlantDetails: RootlyRoute(
        "plants/{plantId}",
        "{plantName}",
        listOf(navArgument("plantId") { type = NavType.StringType },
            navArgument("plantName") { type = NavType.StringType })
    ){
        fun buildRoute(plantId: String) = "plants/$plantId"
    }

    data object UserDetails : RootlyRoute(
        "user/{userId}",
        "{username}",
        listOf(navArgument("userId") { type = NavType.StringType },
            navArgument("username") { type = NavType.StringType })
    ){
        fun buildRoute(userId: String) = "User/$userId" // TODO route?
    }

    companion object {
        val routes = setOf(Home, Explore, Settings, PlantDetails, UserDetails, AddPlant )
    }
}

@Composable
fun RootlyNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = RootlyRoute.Home.route,
        modifier = modifier
    ) {
        with(RootlyRoute.Home) {
            composable(route) {
                HomeScreen(navController)
            }
        }
        with(RootlyRoute.UserDetails) {
            composable(route, arguments) { backStackEntry ->
                UserDetailsScreen(backStackEntry.arguments?.getString("userId") ?: "")
            }
        }
        with(RootlyRoute.PlantDetails) {
            composable(route, arguments) { backStackEntry ->
                PlantDetailsScreen(backStackEntry.arguments?.getString("plantId") ?: "")
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
        with(RootlyRoute.Explore){
            composable(route){
                ExploreScreen(navController = navController)
            }
        }
    }
}