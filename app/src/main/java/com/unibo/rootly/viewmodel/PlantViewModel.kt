package com.unibo.rootly.viewmodel

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

    fun insertPlant(plant: Plant) = viewModelScope.launch {
        repository.insert(plant)
    }

    fun getPlantsByUser(userId: Int) = repository.getByUser(userId)

}