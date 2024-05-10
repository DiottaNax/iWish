package com.unibo.rootly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.BottomBar
import com.unibo.rootly.viewmodel.FertilizerViewModel
import com.unibo.rootly.viewmodel.PlantViewModel
import com.unibo.rootly.viewmodel.WaterViewModel

enum class Filter(
    val displayedName: String   //TODO: add the filter function
) {
    Favourites("Favourites"),
    Today("Today"),
    ThisWeek("This week"),
    Water("Water"),
    Fertilize("Fertilize")
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    waterViewModel: WaterViewModel,
    fertilizerViewModel: FertilizerViewModel,
    plantViewModel: PlantViewModel
) {
    val userId = 1 //todo make real and check filters

    val soonWater = waterViewModel.getSoonWater(userId).collectAsState(initial = listOf()).value
    val soonFertilizer = fertilizerViewModel.getSoonFertilizer(userId).collectAsState(initial = listOf()).value
    val todayWater = waterViewModel.getTodayWater(userId).collectAsState(initial = listOf()).value
    val todayFertilizer = fertilizerViewModel.getTodayFertilizer(userId).collectAsState(initial = listOf()).value

    var selectedFilters by remember { mutableStateOf(emptyList<Filter>()) }

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(RootlyRoute.AddPlant.route) }
            ) {
                Icon(Icons.Outlined.Add, "Add plant")
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = RootlyRoute.Home
            )
        }
    ) { contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(16.dp, 16.dp),
            modifier = Modifier.padding(contentPadding)
        ) {
            item {
                Text(
                    text = RootlyRoute.Home.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
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
                    items(todayWater.size) {
                        AddTasks(
                            plants = todayWater,
                            activity = "water",
                            date = "today",
                            plantViewModel = plantViewModel,
                            navController = navController
                        )
                    }
                }
                if(toShow.contains(Filter.Fertilize)){
                    items(todayFertilizer.size) {
                        AddTasks(
                            plants = todayFertilizer,
                            activity = "fertilize",
                            date = "today",
                            plantViewModel = plantViewModel,
                            navController = navController
                        )
                    }
                }
            }

            if (toShow.contains(Filter.ThisWeek)){
                if(toShow.contains(Filter.Water)){
                    items(soonWater.size) {
                        AddTasks(plants = soonWater,
                            activity = "water",
                            date = "next 2 days",
                            plantViewModel = plantViewModel,
                            navController = navController
                        )
                    }
                }

                if(toShow.contains(Filter.Fertilize)){
                    items(soonFertilizer.size) {
                        AddTasks(plants = soonFertilizer,
                            activity = "fertilize",
                            date = "next 2 days",
                            plantViewModel = plantViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AddTasks(
    plants: List<Plant>,
    activity: String,
    date: String,
    plantViewModel: PlantViewModel,
    navController: NavHostController
) {
    plants.forEach { plant ->
        ActivityItem(
            plant,
            activity,
            date,
            onClick = {
                plantViewModel.selectPlant(plant)
                navController.navigate(
                    RootlyRoute.PlantDetails.buildRoute(
                        plant.plantId.toString(),
                        plant.plantName
                    )
                )
            }
        )
    }
}

@Composable
fun ActivityItem(
    plant: Plant,
    activity: String,
    date: String,
    onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(110.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = plant.plantName,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = plant.scientificName,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = activity,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = date,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Box(
                modifier = Modifier.aspectRatio(0.75f)
            ) {
                Image(
                    Icons.Outlined.Image,
                    "Travel picture",
                    contentScale = ContentScale.None,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondary)
                )
            }
        }
    }
}

@Composable
fun FilterSelector(
    name: String,
    selected: Boolean,
    onFilterSelected: () -> Unit
) {
    FilterChip(
        onClick = onFilterSelected,
        label = { Text(name) },
        selected = selected,
        leadingIcon = if (selected) {
            { Icon(Icons.Filled.Done, "Done") }
        } else {
            null
        }
    )
}
