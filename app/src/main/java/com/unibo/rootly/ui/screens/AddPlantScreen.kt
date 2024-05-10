package com.unibo.rootly.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.TopBar
import com.unibo.rootly.utils.rememberCameraLauncher
import com.unibo.rootly.utils.rememberPermission
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

    val ctx = LocalContext.current

    val cameraLauncher = rememberCameraLauncher()

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() =
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }


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
                .verticalScroll(rememberScrollState())
        ) {
            // Image section
            if (cameraLauncher.capturedImageUri.path?.isNotEmpty() == true) {
                AsyncImage(
                    ImageRequest.Builder(ctx)
                        .data(cameraLauncher.capturedImageUri)
                        .crossfade(true)
                        .build(),
                    "Plant image",
                    Modifier
                        .clip(RoundedCornerShape(28.dp))
                        .height(256.dp)
                )
            } else {
                Image(
                    Icons.Outlined.Image,
                    contentDescription = "Plant image",
                    modifier = Modifier
                        .clip(RoundedCornerShape(28.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .height(256.dp)
                        .padding(110.dp)
                        .fillMaxWidth()
                )
            }

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
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                OutlinedButton(
                    onClick = ::takePicture,
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