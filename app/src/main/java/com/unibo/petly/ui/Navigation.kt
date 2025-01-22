package com.unibo.petly.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.unibo.petly.ui.screens.AddPetScreen
import com.unibo.petly.ui.screens.HomeScreen
import com.unibo.petly.ui.screens.PetDetailsScreen
import com.unibo.petly.ui.screens.SettingsScreen
import com.unibo.petly.ui.screens.UserProfileScreen
import com.unibo.petly.utils.LocationService
import com.unibo.petly.viewmodel.PetViewModel
import com.unibo.petly.viewmodel.SettingsViewModel
import com.unibo.petly.viewmodel.UserViewModel
import org.koin.androidx.compose.koinViewModel

sealed class PetlyRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
){
    data object Home : PetlyRoute("pets" , "To-do")
    data object Settings : PetlyRoute("settings", "Settings")
    data object AddPet : PetlyRoute("add","Add a pet")
    data object PetDetails: PetlyRoute("pet","Pet details")
    data object UserProfile : PetlyRoute(
        "user/{userId}",
        "Profile",
        listOf(navArgument("userId") { type = NavType.StringType } )
    )
    companion object {
        val routes = setOf(Home, Settings, PetDetails, UserProfile, AddPet)
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun PetlyNavGraph(
    navController: NavHostController,
    settingsVM: SettingsViewModel,
    locationService: LocationService,
    userId: Int,
    modifier: Modifier = Modifier
) {
    val  petViewModel = koinViewModel<PetViewModel>()
    val  userViewModel = koinViewModel<UserViewModel>()
    userViewModel.setUser(userId)

    NavHost(
        navController = navController,
        startDestination = PetlyRoute.Home.route,
        modifier = modifier
    ) {
        with(PetlyRoute.Home) {
            composable(route) {
                HomeScreen(
                    navController,
                    petViewModel,
                    userViewModel
                )
            }
        }
        with(PetlyRoute.UserProfile) {
            composable(route, arguments) {
                UserProfileScreen(
                    navController,
                    userViewModel,
                    locationService
                )
            }
        }
        with(PetlyRoute.PetDetails) {
            composable(route, arguments) {
                val pet = petViewModel.petSelected
                if(pet != null) {
                    PetDetailsScreen(
                        navController,
                        petViewModel
                    )
                }
            }
        }
        with(PetlyRoute.AddPet) {
            composable(route) {
                AddPetScreen(navController, petViewModel, userViewModel)
            }
        }
        with(PetlyRoute.Settings) {
            composable(route) {
                SettingsScreen(
                    navController,
                    settingsVM,
                    settingsVM.state
                )
            }
        }
    }
}