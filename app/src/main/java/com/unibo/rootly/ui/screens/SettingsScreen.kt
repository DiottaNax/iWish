package com.unibo.rootly.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.PetlyRoute
import com.unibo.rootly.ui.composables.BottomBar
import com.unibo.rootly.utils.PermissionStatus
import com.unibo.rootly.utils.rememberPermission
import com.unibo.rootly.viewmodel.SettingsViewModel
import com.unibo.rootly.viewmodel.Theme
import com.unibo.rootly.viewmodel.ThemeState
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(
    navController: NavHostController,
    vm: SettingsViewModel,
    state: ThemeState
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Notification
    var showNotificationDeniedAlert by remember { mutableStateOf(false) }
    var showNotificationPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

    val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermission(
            Manifest.permission.POST_NOTIFICATIONS
        ) { status ->
            when (status) {
                PermissionStatus.Granted -> {}
                PermissionStatus.Denied ->
                    showNotificationDeniedAlert = true
                PermissionStatus.PermanentlyDenied ->
                    showNotificationPermanentlyDeniedSnackbar = true
                PermissionStatus.Unknown -> {}
            }
        }
    } else { null }

    fun checkNotificationPermission() {
        notificationPermission?.let {
            if (notificationPermission.status.isGranted) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Notification permission already granted",
                        duration = SnackbarDuration.Short
                    )
                }
            } else {
                notificationPermission.launchPermissionRequest()
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = PetlyRoute.Settings
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Select your theme:",
                style = MaterialTheme.typography.bodyMedium
            )
            Theme.entries.forEach { theme ->
                val selected = (theme == state.theme)

                FilterChip(
                    onClick = { vm.setTheme(theme) },
                    label = {
                        Text(
                            text = theme.toString(),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    selected = selected,
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Chosen icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
            }
            Spacer(Modifier.height(16.dp))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Text(
                    text = "Check your permission:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = { checkNotificationPermission() }
                ) {
                    Text(
                        text = "Notification",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    notificationPermission?.let {
        if (showNotificationDeniedAlert) {
            AlertDialog(
                title = { Text("Notification permission denied") },
                text = { Text("Without notification permission, the application cannot send you information.") },
                confirmButton = {
                    TextButton(onClick = {
                        notificationPermission.launchPermissionRequest()
                        showNotificationDeniedAlert = false
                    }) {
                        Text("Grant")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showNotificationDeniedAlert = false }) {
                        Text("Dismiss")
                    }
                },
                onDismissRequest = { showNotificationDeniedAlert = false }
            )
        }

        if (showNotificationPermanentlyDeniedSnackbar) {
            LaunchedEffect(snackbarHostState) {
                val res = snackbarHostState.showSnackbar(
                    "Notification permission is recommended.",
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
                showNotificationPermanentlyDeniedSnackbar = false
            }
        }
    }
}