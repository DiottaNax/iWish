package com.unibo.rootly.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.R
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

data class PlantCard(
    val plant: Plant,
    val activity: String,
    val date: LocalDate?
)

const val WATER = "water"
const val FERTILIZE = "fertilize"

class PlantViewModel : ViewModel(), KoinComponent {
    private val plantRepository: PlantRepository by inject()
    private val waterRepository: WaterRepository by inject()
    private val fertilizerRepository: FertilizerRepository by inject()
    private val speciesRepository: SpeciesRepository by inject()
    private val receivedRepository: ReceivedRepository by inject()

    private var _plantSelected: Plant? = null

    val plantSelected
        get() = _plantSelected

    fun selectPlant(plant: Plant) {
        _plantSelected = plant
    }

    fun insertPlant(plant: Plant, context: Context) = viewModelScope.launch {
        val newPlant = plant.copy(plantId = plantRepository.insert(plant).toInt())
        insertWater(newPlant, context = context)
        insertFertilizer(newPlant, context = context)

        val plantCount = plantRepository.countByUser(plant.userId)!!

        if (plantCount in 1..9) {
            insertBadge(context,userId = plant.userId , name = context.getString(R.string.badge_1_plant_name))
        }else if( plantCount in 10..49){
            insertBadge(context,userId = plant.userId , name = context.getString(R.string.badge_10_plants_name))
        }else if( plantCount in 50..99){
            insertBadge(context,userId = plant.userId , name = context.getString(R.string.badge_50_plants_name))
        }else if( plantCount >= 100){
            insertBadge(context,userId = plant.userId , name = context.getString(R.string.badge_100_plants_name))
        }
    }

    fun getPlantsByUser(userId: Int) = plantRepository.getPlantsByUser(userId)

    //like
    fun addLike(plantId : Int) = viewModelScope.launch {
        plantRepository.insertLike(plantId)
    }

    fun removeLike(plantId : Int) = viewModelScope.launch {
        plantRepository.removeLike(plantId)
    }

    //dead

    fun addDead(plant : Plant, context: Context) = viewModelScope.launch {
        plantRepository.insertDead(plant.plantId)
        insertBadge(context,userId = plant.userId , name = context.getString(R.string.badge_death_name))
    }

    // Water

    fun insertWater(plant: Plant, date: LocalDate = LocalDate.now(), context: Context) =
        viewModelScope.launch {
            waterRepository.insert(Water(plant.plantId, date))

            if (waterRepository.getTimesWatered(plant.userId) >= 100) {
                insertBadge(context,context.getString(R.string.badge_water_name), plant.userId)
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
            = getAllWater(userId).map{ plants ->
        plants.filter { plant -> plant.date!! <= date }
    }

    fun getWaterBeforeDateFavorite(userId: Int, date: LocalDate)
        = getAllWaterFavorite(userId).map{ plants ->
            plants.filter { plant -> plant.date!! <= date }
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
        speciesRepository.getByName(plant.scientificName)?.waterFrequency?.let { waterFrequency ->
            getLastDate(WATER,plant).plusDays(waterFrequency.toLong())
        }
    }
    //fertilizer

    fun insertFertilizer(plant: Plant, date: LocalDate = LocalDate.now(), context: Context) = viewModelScope.launch {
        fertilizerRepository.insert(Fertilizer(plant.plantId, date))

       if( fertilizerRepository.getTimesFertilized(plant.userId) >= 100) {
            insertBadge(context,context.getString(R.string.badge_fertilizer_name), plant.userId)
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
        = getAllFertilizer(userId).map{ plants ->
            plants.filter { plant -> plant.date!! <= date }
    }

    fun getFertilizerBeforeDateFavorite(userId: Int, date: LocalDate)
        = getAllFertilizerFavorite(userId).map{ plants ->
            plants.filter { plant -> plant.date!! <= date }
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
        speciesRepository.getByName(plant.scientificName)?.fertilizerFrequency?.let { fertilizerFrequency ->
            getLastDate(FERTILIZE,plant).plusDays(fertilizerFrequency.toLong())
        }
    }

    //species

    fun getAllSpeciesNames() = speciesRepository.getAllSpeciesName()

    //badges

    suspend fun insertBadge(context:Context ,name: String, userId: Int) {
        val badges = receivedRepository.getByUser(userId).firstOrNull() ?: emptyList()
        val badgeNames = badges.map { it.name }
        if (name !in badgeNames) {
            receivedRepository.insert(Received(name, userId))
            Notifications.sendNotification(
                name,
                context.getString(R.string.badge_notification_text),
                "Badge Received")
        }
    }

    fun getLastDate(activity: String, plant: Plant): LocalDate {
        return if(activity == WATER){
            waterRepository.getLastWaterDate(plant.plantId) ?: plant.birthday
        }else {
            fertilizerRepository.getLastFertilizeDate(plant.plantId) ?: plant.birthday
        }
    }

    fun getSpecieDetails(scientificName: String) = speciesRepository.getByName(scientificName)

}