package com.unibo.rootly.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityCard(
    title: String,
    subTitle: String,
    activity: String,
    date: String,
    onClick: () -> Unit,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Creates the dismiss state
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if (state == SwipeToDismissBoxValue.StartToEnd ||
                state == SwipeToDismissBoxValue.EndToStart
                ) {   // Activity done
                onCompleted()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(   // Define background color
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.surface
                    SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.primaryContainer
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.primaryContainer
                }, label = "Changing color"
            )
            Box(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxSize()
                    .background(color)
            )
        },
        modifier = modifier
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier.height(110.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                // Data section (Info on the current activity)
                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = subTitle,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = activity,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)

                        )
                        Text(
                            text = date,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            // Image section
            Image(  //TODO: add the right image
                    Icons.Outlined.Image,
                    "Plant image",
                    contentScale = ContentScale.None,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                    modifier = Modifier
                        .aspectRatio(0.75f)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondary)
                )
            }
        }
    }
}