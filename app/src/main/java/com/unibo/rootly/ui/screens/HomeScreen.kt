package com.unibo.rootly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.BottomBar


@Composable
fun HomeScreen(navController: NavHostController) {
    val todayPlants = (1..2).map { "Plant n°$it" } //TODO: add real plants from db
    val soonPlants = (3..20).map { "Plant n°$it" }

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(RootlyRoute.AddPlant.route) }
            ) {
                Icon(Icons.Outlined.Add, "Add Plant")
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                route = RootlyRoute.Home
            )
        }
    ) { contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(32.dp, 32.dp),
            modifier = Modifier.padding(contentPadding)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(top = 80.dp)
                ) {
                    Text(
                        text = RootlyRoute.Home.title,
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        modifier = Modifier.weight(4F)
                    )
                    OutlinedButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .weight(3F)
                            .fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.Add, "Add filter")
                        Text("Filters")
                    }
                }
            }

            items(todayFertilizerPlants) { plant ->
                ActivityItem(plant, "fertilize") { navController.navigate(RootlyRoute.PlantDetails.route) }
            }
            items(todayWaterPlants) { plant ->
                ActivityItem(plant, "water") { navController.navigate(RootlyRoute.PlantDetails.route) }
            }
            items(todayLogsPlants) { plant ->
                ActivityItem(plant, "update") { navController.navigate(RootlyRoute.PlantDetails.route) }
            }

            item {
                Text(
                    text = "Soon",
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
            items(soonPlants) { item ->
                ActivityItem(
                    item,
                    onClick = { navController.navigate(RootlyRoute.PlantDetails.route) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityItem(item: String, onClick: () -> Unit) {
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
                    text = item,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Plant type",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Water",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = TextStyle(fontWeight = FontWeight.Bold)
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