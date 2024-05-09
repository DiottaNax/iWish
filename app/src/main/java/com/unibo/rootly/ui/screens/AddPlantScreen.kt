package com.unibo.rootly.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.TopBar

@Composable
fun AddPlantScreen(
    navController: NavHostController
) {
   Scaffold(
       topBar = { TopBar(navController = navController, currentRoute = RootlyRoute.AddPlant) }
   ) {contentPadding ->
       Box(
           contentAlignment = Alignment.Center,
           modifier = Modifier
               .fillMaxSize()
               .padding(contentPadding)
               .fillMaxWidth()
       ) {
           Text("Add Plant Screen")
       }
   }
}
