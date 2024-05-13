package com.unibo.rootly.ui.screens

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.ImageDisplay
import com.unibo.rootly.ui.composables.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailsScreen(
    navController: NavHostController,
    plant: Plant
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                currentRoute = RootlyRoute.PlantDetails,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = plant.plantName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = plant.scientificName,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Box(
                contentAlignment = Alignment.TopEnd
            ) {
                ImageDisplay(
                    uri = null,
                    contentDescription = "Plant photo",
                    modifier = Modifier
                        .clip(RoundedCornerShape(28.dp))
                        .fillMaxWidth()
                )
                FilledTonalIconButton(
                    onClick = {
                        //TODO: add/remove favourite
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    if(plant.isFavorite) {
                        Icon(Icons.Filled.Favorite, "Favorite")
                    } else {
                        Icon(Icons.Outlined.Favorite, "Favorite")
                    }
                }
            }
            Text(
                text = "Next activities:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Water: Today",
                    style = MaterialTheme.typography.bodyMedium
                )
                AddToCalendarChip(
                    title = "Water ${plant.plantName}",
                    time = System.currentTimeMillis()   //TODO: set right time
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Fertilizer: Today",
                    style = MaterialTheme.typography.bodyMedium
                )
                AddToCalendarChip(
                    title = "Fertilizer ${plant.plantName}",
                    time = System.currentTimeMillis()   //TODO: set right time
                )
            }
        }
    }
}

@Composable
fun AddToCalendarChip(
    title: String,
    time: Long
) {
    val context = LocalContext.current

    AssistChip(
        onClick = {
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time)
            context.startActivity(intent)
        },
        label = {
            Text(text = "Add to your calendar")
        },
        leadingIcon = {
            Icon(
                Icons.Outlined.Event,
                contentDescription = "Add event",
                Modifier.size(AssistChipDefaults.IconSize)
            )
        }
    )
}