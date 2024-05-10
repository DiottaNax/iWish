package com.unibo.rootly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.PlantLog
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.TopBar
import com.unibo.rootly.viewmodel.PlantLogViewModel
import com.unibo.rootly.viewmodel.PlantViewModel

@Composable
fun PlantDetailsScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel,
    plantLogViewModel: PlantLogViewModel,
    s: String,
) {
    val plant: Plant = plantViewModel.plantSelected!!
    val logs = plantLogViewModel.getLogsForPlant(plant.plantId)
        .collectAsState(initial = listOf()).value

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                currentRoute = RootlyRoute.PlantDetails
            )
        }
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp, 0.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = plant.scientificName,
                modifier = Modifier.fillMaxWidth()
            )
            Image(
                Icons.Outlined.Image, //todo metti l'img
                contentDescription = "Plant photo",
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(128.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))

            logs.forEach { log -> LogComponent(log) }
        }
    }
}

@Composable
fun LogComponent(log: PlantLog) {
    Card(
        modifier = Modifier
            .size(110.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Date: ${log.date.toString()}",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = TextStyle(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Description: ${log.description}",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = TextStyle(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                Icons.Outlined.Image, //todo img vera
                contentDescription = "log img",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Height: ${log.height} cm",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = TextStyle(fontWeight = FontWeight.Bold))
        }
    }
}
