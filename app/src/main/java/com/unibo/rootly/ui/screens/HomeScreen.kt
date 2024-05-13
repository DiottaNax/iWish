package com.unibo.rootly.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.ActivityCard
import com.unibo.rootly.ui.composables.BottomBar
import com.unibo.rootly.ui.composables.TopBar
import com.unibo.rootly.viewmodel.PlantViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

enum class Filter(
    val displayedName: String   //TODO: add the filter function
) {
    Favourites("Favourites"),
    Today("Today"),
    ThisWeek("This week"),
    Water("Water"),
    Fertilize("Fertilize")
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val userId = 1 //todo make real and check filters

    var selectedFilters by remember { mutableStateOf(emptyList<Filter>()) }
    var soonWater = emptyList<Plant>()
    var soonFertilizer = emptyList<Plant>()
    var todayWater = emptyList<Plant>()
    var todayFertilizer = emptyList<Plant>()
    var plants = emptyList<PlantCard>().toMutableList()

    if (selectedFilters.contains(Filter.Favourites)){
        soonWater =
            plantViewModel.getFavoritesSoonWater(userId).collectAsState(initial = listOf()).value
        soonFertilizer =
            plantViewModel.getFavoritesSoonFertilizer(userId).collectAsState(initial = listOf()).value
        todayWater =
            plantViewModel.getFavoritesTodayWater(userId).collectAsState(initial = listOf()).value
        todayFertilizer =
            plantViewModel.getFavoritesTodayFertilizer(userId).collectAsState(initial = listOf()).value
    }else{
        soonWater = plantViewModel.getSoonWater(userId).collectAsState(initial = listOf()).value
        soonFertilizer =
            plantViewModel.getSoonFertilizer(userId).collectAsState(initial = listOf()).value
        todayWater =
            plantViewModel.getTodayWater(userId).collectAsState(initial = listOf()).value
        todayFertilizer =
            plantViewModel.getTodayFertilizer(userId).collectAsState(initial = listOf()).value
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(RootlyRoute.AddPlant.route) }
            ) {
                Icon(Icons.Outlined.Add, "Add plant")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopBar(
                    navController = navController,
                    currentRoute = RootlyRoute.Home,
                    scrollBehavior = scrollBehavior
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    Filter.entries.forEach { filter ->
                        FilterSelector(
                            name = filter.displayedName,
                            selected = filter in selectedFilters,
                            onFilterSelected = {
                                selectedFilters = if (filter in selectedFilters) {
                                    selectedFilters - filter
                                } else {
                                    selectedFilters + filter
                                }
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = RootlyRoute.Home
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(16.dp, 16.dp),
            modifier = Modifier.padding(contentPadding)
        ) {
            val toShow = selectedFilters.toMutableList()

            if (!toShow.contains(Filter.ThisWeek)
                && !toShow.contains(Filter.Today)){
                toShow += Filter.Today
                toShow += Filter.ThisWeek
            }

            if (!toShow.contains(Filter.Water)
                && !toShow.contains(Filter.Fertilize)){
                toShow += Filter.Water
                toShow += Filter.Fertilize
            }

            if(toShow.contains(Filter.Today)) {
                if(toShow.contains(Filter.Water)){
                    todayWater.forEach { plant ->
                        plants += PlantCard(plant,WATER , TODAY)
                    }
                }
                if(toShow.contains(Filter.Fertilize)){
                    todayFertilizer.forEach { plant ->
                        plants += PlantCard(plant, FERTILIZE , TODAY)
                    }
                }
            }

            if (toShow.contains(Filter.ThisWeek)){
                if(toShow.contains(Filter.Water)){
                    soonWater.forEach { plant ->
                        plants += PlantCard(plant, WATER , SOON)
                    }
                }

                if(toShow.contains(Filter.Fertilize)){
                    soonFertilizer.forEach { plant ->
                        plants += PlantCard(plant, WATER , SOON)
                    }
                }
            }
            items(plants) { plant ->
                ActivityCard(
                    title = plant.plant.plantName,
                    subTitle = plant.plant.scientificName,
                    activity = plant.activity,
                    date = plant.date,
                    onClick = {
                        navController.navigate(
                            RootlyRoute.PlantDetails.buildRoute(
                                name = plant.plant.plantName,
                                id = plant.plant.plantId.toString()
                            )
                        )
                    },
                    onCompleted = {
                       scope.launch {
                            delay(100.milliseconds)
                            plants.remove(plant)
                            if(plant.activity == FERTILIZE){
                               plantViewModel.insertFertilizer(plant.plant.plantId)
                            }else{
                                plantViewModel.insertWater(plant.plant.plantId)
                            }
                            val snackbarResult = snackbarHostState.showSnackbar(
                                message = "You have completed an activity",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )
                            when (snackbarResult) {
                                SnackbarResult.Dismissed -> null
                                SnackbarResult.ActionPerformed -> {
                                    plants.add(plant)
                                    if(plant.activity == FERTILIZE){
                                        plantViewModel.removeFertilizer(plant.plant.plantId)
                                    }else{
                                        plantViewModel.removeWater(plant.plant.plantId)
                                    }
                                }

                            }

                        }
                    },
                    modifier = Modifier.animateItemPlacement(tween(100))
                )
            }
        }
    }
}

@Composable
fun FilterSelector(
    name: String,
    selected: Boolean,
    onFilterSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        onClick = onFilterSelected,
        label = { Text(name) },
        selected = selected,
        leadingIcon = if (selected) {
            { Icon(Icons.Filled.Done, "Done") }
        } else {
            null
        },
        modifier = modifier
    )
}

data class PlantCard(
    val plant: Plant,
    val activity: String,
    val date: String
)

const val WATER = "water"
const val FERTILIZE = "fertilize"
const val SOON = "in the next few days"
const val TODAY = "today"