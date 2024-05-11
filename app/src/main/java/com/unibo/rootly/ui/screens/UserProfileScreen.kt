package com.unibo.rootly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.unibo.rootly.ui.RootlyRoute
import com.unibo.rootly.ui.composables.BottomBar
import com.unibo.rootly.ui.composables.DefaultCard
import com.unibo.rootly.viewmodel.ReceivedViewModel

@Composable
fun UserProfileScreen(
    navController: NavHostController,
    receivedViewModel: ReceivedViewModel,
    userId: String
) {
    val badgesReceived = receivedViewModel.getReceivedBadgesByUser(1).collectAsState(initial = listOf()).value
    val list = remember { mutableStateListOf("Food", "Book", "Laptop", "Ananas", "Carote", "Magliette", "Maglia") } //TODO: remove example
    //todo real id

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = RootlyRoute.UserProfile
            )
        }
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            Column {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        Icons.Outlined.Image,
                        "Profile Icon",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(90.dp)
                    )
                    Text(
                        text = "Giorgio",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                }
                Text(
                    text = "Your badges:",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(badgesReceived) { badge ->
                        DefaultCard(
                            title = badge.name,
                            body = badge.description
                        )
                    }
                    items(list) { item ->  //TODO: remove example
                        DefaultCard(title = item, body = "Desc")
                    }
                }
            }
        }
    }
}