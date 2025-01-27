package com.unibo.petly.ui.composables

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unibo.petly.MainActivity
import com.unibo.petly.ui.PetlyRoute
import com.unibo.petly.ui.theme.BrightFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    currentRoute: PetlyRoute,
    sharedPreferences: SharedPreferences,
    context: Context,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        title = {
            Text(
                text = currentRoute.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = BrightFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 50.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            if (navController.previousBackStackEntry != null &&
                navBarItems.none { it.route == currentRoute}) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back button",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },

        actions = {
            if (currentRoute == PetlyRoute.UserProfile || currentRoute == PetlyRoute.Settings) {
                IconButton(onClick = {
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putInt("userId", -1)
                    editor.apply()
                    context.startActivity(Intent(context, MainActivity::class.java))
                    (context as Activity).finish()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout button",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        modifier = modifier
    )
}