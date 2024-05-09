package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.Water
import com.unibo.rootly.data.repositories.WaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaterViewModel @Inject constructor(
    private val repository: WaterRepository
): ViewModel() {

    fun insertWater(water: Water) = viewModelScope.launch {
        repository.insert(water)
    }

    fun getSoonWater(userId: Int) = repository.getSoon(userId)

    fun getTodayWater(userId: Int) = repository.getToday(userId)
}