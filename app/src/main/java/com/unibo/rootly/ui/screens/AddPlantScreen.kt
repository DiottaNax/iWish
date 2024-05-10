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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.TopBar
import com.unibo.rootly.viewmodel.PlantViewModel
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
                label = {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = type,
                onValueChange = { type = it },
                label = {
                    Text(
                        text = "Type",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = waterDate,
                onValueChange = { waterDate = it },
                label = {
                    Text(
                        text = "Date (water)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                placeholder = { Text("dd/mm/yyyy") }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = fertilizerDate,
                onValueChange = { fertilizerDate = it },
                label = {
                    Text(
                        text = "Date (fertilizer)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                placeholder = { Text("dd/mm/yyyy") }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Add image",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Button(
                    onClick = {
                        plantViewModel.insertPlant(
                            Plant(
                                userId = 1,
                                plantName = name,
                                birthday = LocalDate.now(), //todo non va
                                scientificName = type,
                                isDead = false,
                            )
                        )
                        navController.navigateUp()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Add plant",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}