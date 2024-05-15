package com.unibo.rootly.ui.screens

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.ui.composables.DropDownMenuField
import com.unibo.rootly.ui.composables.TextField
import com.unibo.rootly.utils.rememberCameraLauncher
import com.unibo.rootly.utils.rememberPermission
import com.unibo.rootly.viewmodel.PlantViewModel
import com.unibo.rootly.viewmodel.UserViewModel
import java.time.LocalDate

@Composable
fun AddPlantScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel,
    userViewModel: UserViewModel
) {
    val possiblePlantTypes = plantViewModel.getAllSpeciesNames()
        .collectAsState(initial = listOf()).value
    var name by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("") }
    var uri: Uri? by remember { mutableStateOf(null) }
    val ctx = LocalContext.current

    // Camera
    val cameraLauncher = rememberCameraLauncher { uri = it }

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() {
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        // Image section
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
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
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                    modifier = Modifier
                        .clip(RoundedCornerShape(28.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .height(256.dp)
                        .padding(110.dp)
                        .fillMaxWidth()
                )
            }
            FilledTonalIconButton(
                onClick = ::takePicture,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    Icons.Default.AddAPhoto,
                    contentDescription = "Add a photo"
                )
            }
        }
        // Data input fields
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onChange = { name = it },
            label = "Name",
            leadingIcon = {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Name",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        )
        DropDownMenuField(
            value = type,
            label = "Type",
            list = possiblePlantTypes,
            onChange = { type = it }
        )
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
                            img = uri?.let {uri.toString()}
                        )
                    )
                    navController.navigateUp()
                } else {
                    Toast.makeText(ctx, "Fill all the data", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Add plant",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}