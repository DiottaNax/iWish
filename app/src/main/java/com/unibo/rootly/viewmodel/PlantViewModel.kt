package com.unibo.rootly.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.Plant
import com.unibo.rootly.data.database.User
import com.unibo.rootly.data.repositories.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantViewModel  @Inject constructor(
    private val repository: PlantRepository
): ViewModel() {

    private var _plantSelected: Plant? = null
    val plantSelected
        get() = _plantSelected

    fun selectPlant(plant: Plant) {
        _plantSelected = plant
    }

    fun insertPlant(plant: Plant) = viewModelScope.launch {
        repository.insert(plant)
    }

    fun getPlantsByUser(userId: Int) = repository.getByUser(userId)
}