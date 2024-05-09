package com.unibo.rootly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.TopBar

@Composable
fun PlantDetailsScreen(
    navController: NavHostController,
    plantId: String
    ) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var pw by rememberSaveable { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {  }
            ) {
                Icon(Icons.Outlined.Mode, "Change")
            }
        },
        topBar = { TopBar(
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
                .padding(36.dp)
                .fillMaxWidth()
        ) {
            Image(
                Icons.Outlined.Image,
                contentDescription = "Plant photo",
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(128.dp)
                    .fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard("Water", {})
                StatCard("Fertilizer", {})
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatCard(item: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Image(
            Icons.Outlined.Image,
            contentDescription = item,
            modifier = Modifier.padding(64.dp, 100.dp)
        )
    }
}