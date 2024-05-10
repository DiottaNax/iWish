package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.Fertilizer
import com.unibo.rootly.data.repositories.FertilizerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FertilizerViewModel @Inject constructor(
    private val repository: FertilizerRepository
): ViewModel() {
    
    fun insertFertilizer(fertilizer: Fertilizer) = viewModelScope.launch {
        repository.insert(fertilizer)
    }

    fun getSoonFertilizer(userId: Int) = repository.getSoon(userId)


    fun getTodayFertilizer(userId: Int) = repository.getToday(userId)


    fun getFavoritesSoonFertilizer(userId: Int) = repository.getFavoritesSoon(userId)

    fun getFavoritesTodayFertilizer(userId: Int) = repository.getFavoritesToday(userId)


}
