package com.unibo.rootly.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.ActivityCard
import com.unibo.rootly.ui.composables.BottomBar
import com.unibo.rootly.viewmodel.FERTILIZE
import com.unibo.rootly.viewmodel.PlantCard
import com.unibo.rootly.viewmodel.PlantViewModel
import com.unibo.rootly.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds

enum class Filter(
    val displayedName: String
) {
    Favourites("Favourites"),
    Today("Today"),
    ThisWeek("This week"),
    Water("Water"),
    Fertilize("Fertilize")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel,
    userViewModel: UserViewModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val userId = userViewModel.user!!.userId

    var selectedFilters by remember { mutableStateOf(emptyList<Filter>()) }
    val plants = emptySet<PlantCard>().toMutableSet()

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
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainer)
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
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = RootlyRoute.Home
            )
        }
    ) { contentPadding ->

        plants.clear()
        if(selectedFilters.contains(Filter.Water)){
            plants.addAll(getDueWater(selectedFilters,plantViewModel,userId)
                .collectAsState(initial = listOf()).value)
        }else if(selectedFilters.contains(Filter.Fertilize)){
            plants.addAll(getDueFertilizer(selectedFilters,plantViewModel,userId)
                .collectAsState(initial = listOf()).value)
        }else{
            plants.addAll(getDueWater(selectedFilters, plantViewModel, userId)
                .collectAsState(initial = listOf()).value)
            plants.addAll(getDueFertilizer(selectedFilters, plantViewModel, userId)
                .collectAsState(initial = listOf()).value)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp, 16.dp),
            modifier = Modifier.padding(contentPadding)
        ) {

            items(plants.toMutableList().sortedBy { it.date },
                key = {"${it.plant.plantId}${it.activity}${it.date}${it.plant.plantName}"}
            ) { plant ->
                ActivityCard(
                    title = plant.plant.plantName,
                    subTitle = plant.plant.scientificName,
                    activity = plant.activity,
                    date = if (plant.date!! <= LocalDate.now()) "today" else plant.date.toString(),
                    onClick = {
                        plantViewModel.selectPlant(plant.plant)
                        navController.navigate(RootlyRoute.PlantDetails.route)
                    },
                    onCompleted = {
                        val res = plantViewModel.getLastDate(plant.activity, plant.plant) != LocalDate.now()
                        scope.launch {
                            delay(100.milliseconds)
                            if (res) {
                                plants.remove(plant)
                                if (plant.activity == FERTILIZE) {
                                    plantViewModel.insertFertilizer(plant.plant)
                                } else {
                                    plantViewModel.insertWater(plant.plant)
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
                                        if (plant.activity == FERTILIZE) {
                                            plantViewModel.removeFertilizer(plant.plant.plantId)
                                        } else {
                                            plantViewModel.removeWater(plant.plant.plantId)
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(context,
                                    "you already ${plant.activity} ${plant.plant.plantName} today"
                                    , Toast.LENGTH_SHORT).show()
                            }
                        }
                        res
                    },
                    img = plant.plant.img,
                    modifier = Modifier.animateItemPlacement(tween(100))
                )
            }
        }
    }
}

fun getDueFertilizer(selectedFilters: List<Filter>, plantViewModel: PlantViewModel, userId: Int)
        :Flow<List<PlantCard>> {
    if (selectedFilters.contains(Filter.Today)){
        return if(selectedFilters.contains(Filter.Favourites)){
            plantViewModel.getFertilizerBeforeDateFavorite(userId, LocalDate.now())
        }else{
            plantViewModel.getFertilizerBeforeDate(userId, LocalDate.now())
        }
    }else if(selectedFilters.contains(Filter.ThisWeek)){
        return if(selectedFilters.contains(Filter.Favourites)){
            plantViewModel.getFertilizerBeforeDateFavorite(userId, LocalDate.now().plusDays(7L))
        }else{
            plantViewModel.getFertilizerBeforeDate(userId, LocalDate.now().plusDays(7L))
        }
    }else{
        return if(selectedFilters.contains(Filter.Favourites)){
            plantViewModel.getAllFertilizerFavorite(userId)
        }else{
            plantViewModel.getAllFertilizer(userId)
        }
    }
}

fun getDueWater(selectedFilters: List<Filter>, plantViewModel: PlantViewModel, userId: Int)
        :Flow<List<PlantCard>> {
    if (selectedFilters.contains(Filter.Today)){
        return if(selectedFilters.contains(Filter.Favourites)){
            plantViewModel.getWaterBeforeDateFavorite(userId, LocalDate.now())
        }else{
            plantViewModel.getWaterBeforeDate(userId, LocalDate.now())
        }
    }else if(selectedFilters.contains(Filter.ThisWeek)){
        return if(selectedFilters.contains(Filter.Favourites)){
            plantViewModel.getWaterBeforeDateFavorite(userId, LocalDate.now().plusDays(7L))
        }else{
            plantViewModel.getWaterBeforeDate(userId, LocalDate.now().plusDays(7L))
        }
    }else{
        return if(selectedFilters.contains(Filter.Favourites)){
            plantViewModel.getAllWaterFavorite(userId)
        }else{
            plantViewModel.getAllWater(userId)
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