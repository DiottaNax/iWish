package com.unibo.rootly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.TopBar
import com.unibo.rootly.viewmodel.PlantViewModel

@Composable
fun PlantDetailsScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel,
    s: String,
) {
    val plant : Plant = plantViewModel.plantSelected!!
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
        }
    }
}
