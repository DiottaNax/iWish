package com.unibo.petly.ui.screens

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.petly.R
import com.unibo.petly.ui.PetlyRoute
import com.unibo.petly.ui.composables.ActivityCard
import com.unibo.petly.ui.composables.BottomBar
import com.unibo.petly.utils.PermissionStatus
import com.unibo.petly.utils.rememberPermission
import com.unibo.petly.viewmodel.FEEDING
import com.unibo.petly.viewmodel.PetCard
import com.unibo.petly.viewmodel.PetViewModel
import com.unibo.petly.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds

enum class Filter(
    val displayedName: String
) {
    Favourites("Favourites"),
    Today("Today"),
    ThisWeek("This week"),
    Feeding("Feeding"),
    Cleaning("Cleaning")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    petViewModel: PetViewModel,
    userViewModel: UserViewModel,
) {
    val incompatibleFilters: Map<Filter, Filter> = mapOf(Filter.Today to Filter.ThisWeek, Filter.Cleaning to Filter.Feeding, Filter.ThisWeek to Filter.Today, Filter.Feeding to Filter.Cleaning)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val userId = userViewModel.user!!.userId

    var selectedFilters by remember { mutableStateOf(emptyList<Filter>()) }
    val pets = emptySet<PetCard>().toMutableSet()

    fun getDefaultImageFromPetType(petType: String): String? {
        val basePath = "android.resource://${context.packageName}/"

        return when(petType.lowercase()) {
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

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(PetlyRoute.AddPet.route) }
            ) {
                Icon(Icons.Outlined.Add, "Add pet")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(horizontal = 16.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                val incompatibleWithSelectedFilters = selectedFilters.map {
                    incompatibleFilters[it]
                    }
                    .toSet()
                Filter.entries.filter {
                    it !in incompatibleWithSelectedFilters
                }.forEach { filter ->
                    FilterSelector(
                        name = filter.displayedName,
                        selected = filter in selectedFilters,
                        onFilterSelected = {
                            selectedFilters = if (filter in selectedFilters) {
                                selectedFilters - filter
                            } else {
                                selectedFilters + filter
                            }
                        }
                    )
                }
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                currentRoute = PetlyRoute.Home
            )
        }
    ) { contentPadding ->

        pets.clear()
        if(selectedFilters.contains(Filter.Cleaning)){
            pets.addAll(
                getDueCleaning(selectedFilters,petViewModel,userId)
                .collectAsState(initial = listOf()).value)
        }else if(selectedFilters.contains(Filter.Feeding)){
            pets.addAll(
                getDueFeeding(selectedFilters,petViewModel,userId)
                .collectAsState(initial = listOf()).value)
        }else{
            pets.addAll(
                getDueCleaning(selectedFilters, petViewModel, userId)
                .collectAsState(initial = listOf()).value)
            pets.addAll(
                getDueFeeding(selectedFilters, petViewModel, userId)
                .collectAsState(initial = listOf()).value)
        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp, 16.dp),
            modifier = Modifier.padding(contentPadding)
        ) {
            items(pets.toMutableList().sortedBy { it.date },
                key = {"${it.pet.petId}${it.activity}${it.date}${it.pet.petName}"}
            ) { petCard ->
                ActivityCard(
                    title = petCard.pet.petName,
                    subTitle = petCard.pet.specie,
                    activity = petCard.activity,
                    date = if (petCard.date!! <= LocalDate.now()) "today" else petCard.date.format(
                        DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                    onClick = {
                        petViewModel.selectPet(petCard.pet)
                        navController.navigate(PetlyRoute.PetDetails.route)
                    },
                    onCompleted = {
                        scope.launch {
                            delay(100.milliseconds)
                            if (petViewModel.getLastDate(petCard.activity, petCard.pet) != LocalDate.now()) {
                                pets.remove(petCard)
                                if (petCard.activity == FEEDING) {
                                    petViewModel.insertFeeding(petCard.pet,context = context)
                                } else {
                                    petViewModel.insertCleaning(petCard.pet,context = context)
                                }
                                val snackbarResult = snackbarHostState.showSnackbar(
                                    message = "You have completed an activity",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                                when (snackbarResult) {
                                    SnackbarResult.Dismissed -> {}
                                    SnackbarResult.ActionPerformed -> {
                                        pets.add(petCard)
                                        if (petCard.activity == FEEDING) {
                                            petViewModel.removeFeeding(petCard.pet.petId)
                                        } else {
                                            petViewModel.removeCleaning(petCard.pet.petId)
                                        }
                                    }
                                }
                            } else {
                                snackbarHostState.showSnackbar(
                                    message = "You already ${if(petCard.activity == "cleaning") "cleaned" else "fed"} ${petCard.pet.petName} today",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    img = if(petCard.pet.img.isNullOrBlank()) getDefaultImageFromPetType(petCard.pet.specie) else petCard.pet.img,
                    modifier = Modifier.animateItemPlacement(tween(100))
                )
            }
        }
    }
}

fun getDueFeeding(selectedFilters: List<Filter>, petViewModel: PetViewModel, userId: Int)
        :Flow<List<PetCard>> {
    if (selectedFilters.contains(Filter.Today)){
        return if(selectedFilters.contains(Filter.Favourites)){
            petViewModel.getFeedingBeforeDateFavorite(userId, LocalDate.now())
        }else{
            petViewModel.getFeedingBeforeDate(userId, LocalDate.now())
        }
    }else if(selectedFilters.contains(Filter.ThisWeek)){
        return if(selectedFilters.contains(Filter.Favourites)){
            petViewModel.getFeedingBeforeDateFavorite(userId, LocalDate.now().plusDays(7L))
        }else{
            petViewModel.getFeedingBeforeDate(userId, LocalDate.now().plusDays(7L))
        }
    }else{
        return if(selectedFilters.contains(Filter.Favourites)){
            petViewModel.getAllFeedingFavorite(userId)
        }else{
            petViewModel.getAllFeeding(userId)
        }
    }
}

fun getDueCleaning(selectedFilters: List<Filter>, petViewModel: PetViewModel, userId: Int)
        :Flow<List<PetCard>> {
    if (selectedFilters.contains(Filter.Today)){
        return if(selectedFilters.contains(Filter.Favourites)){
            petViewModel.getCleaningBeforeDateFavorite(userId, LocalDate.now())
        }else{
            petViewModel.getCleaningBeforeDate(userId, LocalDate.now())
        }
    }else if(selectedFilters.contains(Filter.ThisWeek)){
        return if(selectedFilters.contains(Filter.Favourites)){
            petViewModel.getCleaningBeforeDateFavorite(userId, LocalDate.now().plusDays(7L))
        }else{
            petViewModel.getCleaningBeforeDate(userId, LocalDate.now().plusDays(7L))
        }
    }else{
        return if(selectedFilters.contains(Filter.Favourites)){
            petViewModel.getAllCleaningFavorite(userId)
        }else{
            petViewModel.getAllCleaning(userId)
        }
    }
}

@Composable
fun FilterSelector(
    name: String,
    selected: Boolean,
    onFilterSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        onClick = onFilterSelected,
        label = { Text(name) },
        selected = selected,
        leadingIcon = if (selected) {
            { Icon(Icons.Filled.Done, "Done") }
        } else {
            null
        },
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            labelColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
//            disabledLeadingIconColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
//            selectedLeadingIconColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
}