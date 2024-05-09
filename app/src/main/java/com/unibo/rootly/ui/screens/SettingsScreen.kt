package com.unibo.rootly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

//TODO copiato dalla prof
@Composable
fun SettingsScreen(
    navController: NavHostController,
    //state: SettingsState,
    //onUsernameChanged: (String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var pw by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { TopBar(
            navController = navController,
            currentRoute = RootlyRoute.Settings
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
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(128.dp)
            )

            // Data input fields
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = pw,
                onValueChange = { pw = it },
                label = { Text("Password") },
            )
            //TODO: add radio buttons for choosing theme


            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Save")
            }
        }
    }
}