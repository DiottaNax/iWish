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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.ImageDisplay
import com.unibo.rootly.ui.composables.TopBar
import com.unibo.rootly.viewmodel.PlantViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailsScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel
) {
    val plant = plantViewModel.plantSelected!!
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var isFavorite by remember { mutableStateOf(plant.isFavorite) }


    var nextWaterDate by remember { mutableStateOf<LocalDate?>(null) }
    var nextFertilizeDate by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            nextWaterDate = plantViewModel.getNextWater(plant)
            nextFertilizeDate = plantViewModel.getNextFertilize(plant)
        }
    }

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
                        if (isFavorite) {
                            plantViewModel.removeLike(plant.plantId)
                        } else {
                            plantViewModel.addLike(plant.plantId)
                        }
                        isFavorite = !isFavorite
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    if(isFavorite) {
                        Icon(Icons.Filled.Favorite, "add to Favorites")
                    } else {
                        Icon(Icons.Outlined.FavoriteBorder , "remove from Favorites")
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
                    text = "Water: $nextWaterDate",
                    style = MaterialTheme.typography.bodyMedium
                )
                AddToCalendarChip(
                    title = "Water ${plant.plantName}",
                    time = nextWaterDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
                        ?.toEpochMilli() ?: System.currentTimeMillis()
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Fertilizer: $nextFertilizeDate",
                    style = MaterialTheme.typography.bodyMedium
                )
                AddToCalendarChip(
                    title = "Fertilizer ${plant.plantName}",
                    time = nextFertilizeDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
                        ?.toEpochMilli() ?: System.currentTimeMillis()
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        plantViewModel.addDead(plant)
                        navController.navigateUp()
                    }
                ) {
                    Text("Mark as dead")
                }
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