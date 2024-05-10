package com.unibo.rootly.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.User
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.TopBar
import com.unibo.rootly.viewmodel.PlantViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun AddPlantScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel
) {
    var name by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("") }
    var waterDate by rememberSaveable { mutableStateOf("") }
    var fertilizerDate by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { TopBar(
            navController = navController,
            currentRoute = RootlyRoute.AddPlant
            )
        }
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp, 0.dp)
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

            // Data input fields
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = type,
                onValueChange = { type = it },
                label = { Text("Type") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = waterDate,
                onValueChange = { waterDate = it },
                label = { Text("Date (Water)") },
                placeholder = { Text("dd/mm/yyyy") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = fertilizerDate,
                onValueChange = { fertilizerDate = it },
                label = { Text("Date (Fertilizer)") },
                placeholder = { Text("dd/mm/yyyy") }
            )

            Button(
                onClick = {
                          plantViewModel.insertPlant(Plant(
                              userId = 1,
                              plantName = name,
                              birthday = LocalDate.now(), //todo non va
                              scientificName = type,
                              isDead = false,
                          ))
                        navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Add")
            }
        }
    }
}