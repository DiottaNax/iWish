package com.unibo.rootly.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.ui.composables.DropDownMenuField
import com.unibo.rootly.ui.composables.TextField
import com.unibo.rootly.utils.PermissionStatus
import com.unibo.rootly.utils.rememberCameraLauncher
import com.unibo.rootly.utils.rememberPermission
import com.unibo.rootly.viewmodel.PlantViewModel
import com.unibo.rootly.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun AddPlantScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel,
    userViewModel: UserViewModel
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val possiblePlantTypes = plantViewModel
        .getAllSpeciesNames()
        .collectAsState(initial = listOf())
        .value

    //Form
    var name by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("") }
    var uri: Uri? by remember { mutableStateOf(null) }

    // Camera
    val cameraLauncher = rememberCameraLauncher { uri = it }
    var showCameraDeniedAlert by remember { mutableStateOf(false) }
    var showCameraPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        when (status) {
            PermissionStatus.Granted -> {
                cameraLauncher.captureImage()
            }
            PermissionStatus.Denied ->
                showCameraDeniedAlert = true
            PermissionStatus.PermanentlyDenied ->
                showCameraPermanentlyDeniedSnackbar = true
            PermissionStatus.Unknown -> {}
        }
    }

    fun takePicture() {
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }
    }

    // Screen UI
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
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
                            .fillMaxWidth()
                            .heightIn(min = 256.dp,  max = 435.dp),
                        contentScale = ContentScale.Crop
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
                    if (name != "" && type != "") {
                        plantViewModel.insertPlant(
                            Plant(
                                userId = userViewModel.user!!.userId,
                                plantName = name,
                                birthday = LocalDate.now(),
                                scientificName = type,
                                isDead = false,
                                img = uri?.let { uri.toString() }
                            ),context = ctx
                        )
                        navController.navigateUp()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "All fields are required",
                                duration = SnackbarDuration.Short
                            )
                        }
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

    if (showCameraDeniedAlert) {
        AlertDialog(
            title = { Text("Camera permission denied") },
            text = { Text("Camera permission is required to add your photo in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    cameraPermission.launchPermissionRequest()
                    showCameraDeniedAlert = false
                }) {
                    Text("Grant")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCameraDeniedAlert = false }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { showCameraDeniedAlert = false }
        )
    }

    if (showCameraPermanentlyDeniedSnackbar) {
        LaunchedEffect(snackbarHostState) {
            val res = snackbarHostState.showSnackbar(
                "Camera permission is required.",
                "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", ctx.packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                if (intent.resolveActivity(ctx.packageManager) != null) {
                    ctx.startActivity(intent)
                }
            }
            showCameraPermanentlyDeniedSnackbar = false
        }
    }
}