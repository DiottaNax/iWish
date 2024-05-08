package com.unibo.rootly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.BottomBar

@Composable
fun HomeScreen(
    navController: NavHostController
) {
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(contentPadding)
                .fillMaxWidth()
        ) {
            Text("Home")
        }
    }
}

