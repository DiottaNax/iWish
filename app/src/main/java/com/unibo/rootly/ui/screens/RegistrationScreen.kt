package com.unibo.rootly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute

@Composable
fun RegistrationScreen(navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("Register")
        Button(
            onClick = { navController.navigate(RootlyRoute.Login.route) },
            modifier = Modifier.weight(1F)
        ) {
            Text("Login")
        }
        Button(
            onClick = { navController.navigate(RootlyRoute.Home.route) },
            modifier = Modifier.weight(1F)
        ) {
            Text("Home")
        }
    }
}