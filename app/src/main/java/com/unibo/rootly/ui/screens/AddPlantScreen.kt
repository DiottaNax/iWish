package com.unibo.rootly.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.TopBar
import com.unibo.rootly.utils.Notifications
import com.unibo.rootly.utils.rememberCameraLauncher
import com.unibo.rootly.utils.rememberPermission
import com.unibo.rootly.viewmodel.PlantViewModel
import com.unibo.rootly.viewmodel.UserViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlantScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel,
    userViewModel: UserViewModel
) {
    var name by rememberSaveable { mutableStateOf("") }
    val possiblePlantTypes = plantViewModel.getAllSpeciesNames()
        .collectAsState(initial = listOf()).value

    var expanded by rememberSaveable { mutableStateOf(false) }

    var type by rememberSaveable { mutableStateOf("") }

    // Function to handle selection of plant type and close menu
    val onPlantTypeSelected: (String) -> Unit = {
        type = it
        expanded = false
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
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
        topBar = {
            TopBar(
                navController = navController,
                currentRoute = RootlyRoute.AddPlant,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
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
                    Modifier.clip(RoundedCornerShape(28.dp))
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
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier.fillMaxWidth()
            ){
                TextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    possiblePlantTypes.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = { onPlantTypeSelected(item) }
                        )
                    }
                }
            }
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
                              if (name != "" && type != ""){
                                  plantViewModel.insertPlant(
                                      Plant(
                                          userId = userViewModel.user!!.userId,
                                          plantName = name,
                                          birthday = LocalDate.now(),
                                          scientificName = type,
                                          isDead = false,
                                      )
                                  )
                                  navController.navigateUp()
                              }else{
                                  Toast.makeText(ctx, "Fill all the data", Toast.LENGTH_SHORT).show()
                              }
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