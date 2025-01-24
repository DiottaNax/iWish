package com.unibo.petly.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.unibo.petly.R
import com.unibo.petly.data.database.Pet
import com.unibo.petly.data.database.PetSpecies
import com.unibo.petly.ui.composables.ImageDisplay
import com.unibo.petly.viewmodel.PetViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun PetDetailsScreen(
    navController: NavHostController,
    petViewModel: PetViewModel
) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    val pet = petViewModel.petSelected!!
    var isFavorite by remember { mutableStateOf(pet.isFavorite) }
    var nextFeedingDate by remember { mutableStateOf<LocalDate?>(null) }
    var nextCleaningDate by remember { mutableStateOf<LocalDate?>(null) }
    var specie by remember { mutableStateOf<PetSpecies?>(null) }

    fun petImageOrDefault(pet: Pet): String? {
        val basePath = "android.resource://${context.packageName}/"

        return if (!pet.img.isNullOrBlank()) pet.img
        else when (pet.specie.lowercase()) {
            "dog" -> basePath.plus(R.drawable.dog_default_image)
            "cat" -> basePath.plus(R.drawable.cat_default_image)
            "parrot" -> basePath.plus(R.drawable.parrot_default_image)
            "goldfish" -> basePath.plus(R.drawable.goldfish_default_image)
            "hamster" -> basePath.plus(R.drawable.hamster_default_image)
            "snake" -> basePath.plus(R.drawable.snake_default_image)
            "rabbit" -> basePath.plus(R.drawable.rabbit_default_image)
            else -> null
        }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            nextFeedingDate = petViewModel.getNextFeeding(pet)
            nextCleaningDate = petViewModel.getNextCleaning(pet)
            specie = petViewModel.getSpecieDetails(pet.specie)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        // Nome centrato sopra l'immagine
        Text(
            text = pet.petName,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            fontSize = 45.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp), // Margine inferiore più piccolo
            textAlign = TextAlign.Center
        )
        // Immagine con pulsante "Preferito"
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            ImageDisplay(
                uri = petImageOrDefault(pet)?.let { Uri.parse(petImageOrDefault(pet)) },
                contentDescription = "Pet photo",
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .fillMaxWidth()
                    .heightIn(min = 256.dp, max = 435.dp),
                defaultHigh = 256.dp
            )
            FilledTonalIconButton(
                onClick = {
                    if (isFavorite) {
                        petViewModel.removeLike(pet.petId)
                    } else {
                        petViewModel.addLike(pet.petId)
                    }
                    isFavorite = !isFavorite
                },
                modifier = Modifier.padding(4.dp),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "add to Favorites",
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "remove from Favorites",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        // Scritta "Added on"
        Text(
            text = "Added on ${pet.birthday.format(formatter)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        // Specie sotto "Added on"
        Text(
            text = "(${pet.specie})",
            fontSize = 30.sp,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        // Informazioni sull'animale
        Text(
            text = "Pet Info:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Diet: ${specie?.dietType ?: "No diet available"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Living temperature: " +
                    "${specie?.minTemperature}°  –  " +
                    "${specie?.maxTemperature}°",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "\nNext activities:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary
        )
        // Attività di alimentazione e pulizia
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Feeding: ${nextFeedingDate?.format(formatter)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            AddToCalendarButton(
                title = "Feeding ${pet.petName}",
                time = nextFeedingDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
                    ?.toEpochMilli() ?: System.currentTimeMillis()
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Cleaning: ${nextCleaningDate?.format(formatter)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            AddToCalendarButton(
                title = "Cleaning ${pet.petName}",
                time = nextCleaningDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
                    ?.toEpochMilli() ?: System.currentTimeMillis()
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigateUp()
                petViewModel.removePet(pet)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text("Remove pet")
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun AddToCalendarButton(
    title: String,
    time: Long
) {
    val context = LocalContext.current

    Box(
        Modifier.clickable {
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time)
            context.startActivity(intent)
        }
    ) {
        Icon(
            Icons.Outlined.Event,
            contentDescription = "Add event",
        )
    }
}
