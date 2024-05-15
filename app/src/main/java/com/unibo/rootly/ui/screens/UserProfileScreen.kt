package com.unibo.rootly.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.MainActivity
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.BottomBar
import com.unibo.rootly.ui.composables.DefaultCard
import com.unibo.rootly.ui.composables.ImageDisplay
import com.unibo.rootly.ui.composables.TopBar
import com.unibo.rootly.utils.LocationService
import com.unibo.rootly.utils.PermissionStatus
import com.unibo.rootly.utils.StartMonitoringResult
import com.unibo.rootly.utils.rememberPermission
import com.unibo.rootly.viewmodel.UserViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    sharedPreferences: SharedPreferences,
    locationService: LocationService,
    userId: String
) {
    val user = userViewModel.user!!
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val badgesReceived = userViewModel.getReceivedBadgesByUser(user.userId).collectAsState(initial = listOf()).value

    var photoUri: Uri? by remember { mutableStateOf(null) } //TODO: save photo
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { uri -> photoUri = uri }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var showLocationDisabledAlert by remember { mutableStateOf(false) }
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

    val locationPermission = rememberPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) { status ->
        when (status) {
            PermissionStatus.Granted -> {
                val res = locationService.requestCurrentLocation()
                showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
            }

            PermissionStatus.Denied ->
                showPermissionDeniedAlert = true

            PermissionStatus.PermanentlyDenied ->
                showPermissionPermanentlyDeniedSnackbar = true

            PermissionStatus.Unknown -> {}
        }
    }

    fun requestLocation() {
        if (locationPermission.status.isGranted) {
            val res = locationService.requestCurrentLocation()
            showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                currentRoute = RootlyRoute.UserProfile,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = RootlyRoute.UserProfile
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier.size(160.dp)
                    ) {
                        ImageDisplay(
                            uri = photoUri, 
                            contentDescription = "Profile photo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                        FilledTonalIconButton(
                            onClick = {
                                launcher.launch(
                                    PickVisualMediaRequest(
                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                        ) {
                            Icon(Icons.Filled.AddPhotoAlternate, "Add a photo")
                        }
                    }
                    Button(
                        onClick = {
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putInt("userId", -1)
                            userViewModel.logout()
                            editor.apply()
                            context.startActivity(Intent(context, MainActivity::class.java))
                            (context as Activity).finish()
                        }
                    ) {
                        Text(text = "Logout")
                    }
                    Text(
                        text = user.username,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = getLocationName(
                            latitude = locationService.coordinates?.latitude ?: 0.toDouble(),
                            longitude = locationService.coordinates?.longitude ?: 0.toDouble()
                        ),
                        modifier = Modifier.clickable { requestLocation() },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Your badges:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                    )
                }
            }
            items(badgesReceived, key = { it.name }){ badge ->
                DefaultCard(
                    title = badge.name,
                    body = badge.description
                )
            }
        }
    }

    if (showLocationDisabledAlert) {
        AlertDialog(
            title = { Text("Location disabled") },
            text = { Text("Location must be enabled to get your current location in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationService.openLocationSettings()
                    showLocationDisabledAlert = false
                }) {
                    Text("Enable")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLocationDisabledAlert = false }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { showLocationDisabledAlert = false }
        )
    }

    if (showPermissionDeniedAlert) {
        AlertDialog(
            title = { Text("Location permission denied") },
            text = { Text("Location permission is required to get your current location in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationPermission.launchPermissionRequest()
                    showPermissionDeniedAlert = false
                }) {
                    Text("Grant")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDeniedAlert = false }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { showPermissionDeniedAlert = false }
        )
    }

    val ctx = LocalContext.current
    if (showPermissionPermanentlyDeniedSnackbar) {
        LaunchedEffect(snackbarHostState) {
            val res = snackbarHostState.showSnackbar(
                "Location permission is required.",
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
            showPermissionPermanentlyDeniedSnackbar = false
        }
    }
}

@Composable
fun getLocationName(latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
    return if (!addresses.isNullOrEmpty()) {
        val city = addresses[0].locality ?: "City not found"
        val state = addresses[0].adminArea ?: "State not found"
        "$city, $state"
    } else {
        "Location not found, click to sync"
    }
}