package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.Fertilizer
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.Received
import com.unibo.rootly.data.database.Water
import com.unibo.rootly.data.repositories.FertilizerRepository
import com.unibo.rootly.data.repositories.PlantRepository
import com.unibo.rootly.data.repositories.ReceivedRepository
import com.unibo.rootly.data.repositories.SpeciesRepository
import com.unibo.rootly.data.repositories.WaterRepository
import com.unibo.rootly.utils.Notifications
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

data class PlantCard(
    val plant: Plant,
    val activity: String,
    val date: LocalDate?
)

const val WATER = "water"
const val FERTILIZE = "fertilize"
@HiltViewModel
class PlantViewModel  @Inject constructor(
    private val plantRepository: PlantRepository,
    private val waterRepository: WaterRepository,
    private val fertilizerRepository: FertilizerRepository,
    private val speciesRepository: SpeciesRepository,
    private val receivedRepository: ReceivedRepository

): ViewModel() {

    private var _plantSelected: Plant? = null

    val plantSelected
        get() = _plantSelected

    fun selectPlant(plant: Plant) {
        _plantSelected = plant
    }

    fun insertPlant(plant: Plant) = viewModelScope.launch {
        val newPlant = plant.copy(plantId = plantRepository.insert(plant).toInt())
        insertWater(newPlant)
        insertFertilizer(newPlant)

        val plantCount = plantRepository.countByUser(plant.userId)!!

        if (plantCount == 1) {
            insertBadge(userId = plant.userId , name = "First Timer")
        }else if( plantCount >= 100){
            insertBadge(userId = plant.userId , name = "Botanical Master")
        }else if( plantCount >= 50){
            insertBadge(userId = plant.userId , name = "Green Thumb")
        }else if( plantCount >= 10){
            insertBadge(userId = plant.userId , name = "Sprout Scout")
        }
    }

    //like
    fun addLike(plantId : Int) = viewModelScope.launch {
        plantRepository.insertLike(plantId)
    }

    fun removeLike(plantId : Int) = viewModelScope.launch {
        plantRepository.removeLike(plantId)
    }

    //dead

    fun addDead(plant : Plant) = viewModelScope.launch {
        plantRepository.insertDead(plant.plantId)
        insertBadge(userId = plant.userId , name = "Heartfelt Mourner")
    }

    // Water

    fun insertWater(plant: Plant, date: LocalDate = LocalDate.now()) = viewModelScope.launch {
        waterRepository.insert(Water(plant.plantId, date))

        val timesWatered = waterRepository.getTimesWatered(plant.userId)

        val badgesList = mutableListOf<String>()
        receivedRepository.getByUser(plant.userId).collect { badges ->
            badgesList.addAll(badges.map { it.name })

            if ("Water Warrior" !in badgesList && timesWatered >= 100) {
                insertBadge("Water Warrior", plant.userId)
            }
        }
    }

    fun getAllWater(userId: Int) = waterRepository.getAll(userId).map { plants ->
        plants.map { plant ->
            PlantCard(
                plant = plant,
                activity = WATER,
                date = getNextWater(plant)
            )
        }
    }

    fun getWaterBeforeDate(userId: Int, date: LocalDate)
    = waterRepository.getDuesBeforeDate(userId,date).map { plants ->
        plants.map { plant ->
            PlantCard(
                plant = plant,
                activity = WATER,
                date = getNextWater(plant)
            )
        }
    }

    fun getWaterBeforeDateFavorite(userId: Int, date: LocalDate) =
        waterRepository.getDuesBeforeDateFavorites(userId, date).map { plants ->
        plants.map { plant ->
            PlantCard(
                plant = plant,
                activity = WATER,
                date = getNextWater(plant)
            )
        }
    }

    fun getAllWaterFavorite(userId: Int) = waterRepository.getAllFavorites(userId).map { plants ->
        plants.map { plant ->
            PlantCard(
                plant = plant,
                activity = WATER,
                date = getNextWater(plant)
            )
        }
    }

    fun removeWater(plantId: Int, date: LocalDate = LocalDate.now()) = viewModelScope.launch {
        waterRepository.remove(plantId, date)
    }

    suspend fun getNextWater(plant: Plant): LocalDate? = withContext(Dispatchers.IO) {
        val lastWaterDate = waterRepository.getLastWaterDate(plant.plantId) ?: plant.birthday
        speciesRepository.getByName(plant.scientificName)?.waterFrequency?.let { waterFrequency ->
            lastWaterDate.plusDays(waterFrequency.toLong())
        }
    }
    //fertilizer

    fun insertFertilizer(plant: Plant, date: LocalDate = LocalDate.now()) = viewModelScope.launch {
        fertilizerRepository.insert(Fertilizer(plant.plantId, date))

        val timesFertilized = fertilizerRepository.getTimesFertilized(plant.userId)

        val badgesList = mutableListOf<String>()
        receivedRepository.getByUser(plant.userId).collect { badges ->
            badgesList.addAll(badges.map { it.name })

            if ("Zen Gardener" !in badgesList && timesFertilized >= 100) {
                insertBadge("Zen Gardener", plant.userId)
            }
        }
    }

    fun getAllFertilizer(userId: Int) = fertilizerRepository.getAll(userId).map { plants ->
        plants.map { plant ->
            PlantCard(
                plant = plant,
                activity = FERTILIZE,
                date = getNextFertilize(plant)
            )
        }
    }

    fun getFertilizerBeforeDate(userId: Int, date: LocalDate)
            = fertilizerRepository.getDuesBeforeDate(userId,date).map { plants ->
        plants.map { plant ->
            PlantCard(
                plant = plant,
                activity = FERTILIZE,
                date = getNextFertilize(plant)
            )
        }
    }

    fun getFertilizerBeforeDateFavorite(userId: Int, date: LocalDate) =
        fertilizerRepository.getDuesBeforeDateFavorites(userId, date).map { plants ->
            plants.map { plant ->
                PlantCard(
                    plant = plant,
                    activity = FERTILIZE,
                    date = getNextFertilize(plant)
                )
            }
        }

    fun getAllFertilizerFavorite(userId: Int) =
        fertilizerRepository.getAllFavorites(userId).map { plants ->
        plants.map { plant ->
            PlantCard(
                plant = plant,
                activity = FERTILIZE,
                date = getNextFertilize(plant)
            )
        }
    }

    fun removeFertilizer(plantId: Int, date: LocalDate = LocalDate.now()) = viewModelScope.launch {
        fertilizerRepository.remove(plantId, date)
    }

    suspend fun getNextFertilize(plant: Plant): LocalDate? = withContext(Dispatchers.IO) {
        val lastFertilized = fertilizerRepository.getLastFertilizeDate(plant.plantId) ?: plant.birthday
        speciesRepository.getByName(plant.scientificName)?.fertilizerFrequency?.let { fertilizerFrequency ->
            lastFertilized.plusDays(fertilizerFrequency.toLong())
        }
    }

    //species

    fun getAllSpeciesNames() = speciesRepository.getAllSpeciesName()

    //badges

    private suspend fun insertBadge(name: String, userId: Int) {
        receivedRepository.insert(Received(name, userId))
        val badgeNotificationText = "Congratulations, you received a new badge!" +
                "go to your profile to see it"
        Notifications.sendNotification(name,badgeNotificationText)
    }

}