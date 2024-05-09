package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.PlantLog
import com.unibo.rootly.data.repositories.PlantLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantLogViewModel @Inject constructor(
    private val repository: PlantLogRepository
): ViewModel() {

    fun insertPlantLog(log: PlantLog) = viewModelScope.launch {
        repository.insert(log)
    }

    fun getSoonPlantLog(userId: Int) = repository.getSoon(userId)

    fun getTodayPlantLog(userId: Int) =  repository.getToday(userId)

}