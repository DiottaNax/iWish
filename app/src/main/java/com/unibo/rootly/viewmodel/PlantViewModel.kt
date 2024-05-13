package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.Fertilizer
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.Water
import com.unibo.rootly.data.repositories.FertilizerRepository
import com.unibo.rootly.data.repositories.PlantRepository
import com.unibo.rootly.data.repositories.SpeciesRepository
import com.unibo.rootly.data.repositories.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PlantViewModel  @Inject constructor(
    private val plantRepository: PlantRepository,
    private val waterRepository: WaterRepository,
    private val fertilizerRepository: FertilizerRepository,
    private val speciesRepository: SpeciesRepository

): ViewModel() {

    private var _plantSelected: Plant? = null
    val plantSelected
        get() = _plantSelected

    fun selectPlant(plant: Plant) {
        _plantSelected = plant
    }

    fun insertPlant(plant: Plant) = viewModelScope.launch {
        val plantId = plantRepository.insert(plant)
        insertWater(plantId.toInt())
        insertFertilizer(plantId.toInt())
    }

    fun getPlantsByUser(userId: Int) = plantRepository.getByUser(userId)

    //like
    fun addLike(plantId : Int) = viewModelScope.launch {
        plantRepository.insertLike(plantId)
    }

    fun removeLike(plantId : Int) = viewModelScope.launch {
        plantRepository.removeLike(plantId)
    }

    //dead

    fun addDead(plantId : Int) = viewModelScope.launch {
        plantRepository.insertDead(plantId)
    }

    // Water

    fun insertWater(plantId: Int, date: LocalDate = LocalDate.now()) = viewModelScope.launch {
        waterRepository.insert(Water(plantId, date))
    }

    fun getSoonWater(userId: Int) = waterRepository.getSoon(userId)

    fun getTodayWater(userId: Int) = waterRepository.getToday(userId)

    fun getFavoritesSoonWater(userId: Int) = waterRepository.getFavoritesSoon(userId)

    fun getFavoritesTodayWater(userId: Int) = waterRepository.getFavoritesToday(userId)

    fun removeWater(plantId: Int, date: LocalDate = LocalDate.now()) = viewModelScope.launch {
        waterRepository.remove(plantId, date)
    }

    fun getNextWater(plant: Plant) =
        speciesRepository.getByName(plant.scientificName)?.waterFrequency?.let {
            waterRepository.getLastWaterDate(plant.plantId).plusDays(
                it.toLong()
            )
        }

    //fertilizer

    fun insertFertilizer(plantId: Int, date: LocalDate = LocalDate.now()) = viewModelScope.launch {
        fertilizerRepository.insert(Fertilizer(plantId, date))
    }

    fun getSoonFertilizer(userId: Int) = fertilizerRepository.getSoon(userId)


    fun getTodayFertilizer(userId: Int) = fertilizerRepository.getToday(userId)


    fun getFavoritesSoonFertilizer(userId: Int) = fertilizerRepository.getFavoritesSoon(userId)

    fun getFavoritesTodayFertilizer(userId: Int) = fertilizerRepository.getFavoritesToday(userId)

    fun removeFertilizer(plantId: Int, date: LocalDate = LocalDate.now()) = viewModelScope.launch {
        fertilizerRepository.remove(plantId, date)
    }

    fun getNextFertilize(plant: Plant) =
        speciesRepository.getByName(plant.scientificName)?.fertilizerFrequency?.let {
            fertilizerRepository.getLastFertilizeDate(plant.plantId).plusDays(
                it.toLong()
            )
        }

    //species

    fun getAllSpeciesName() = speciesRepository.getAllSpeciesName()

}