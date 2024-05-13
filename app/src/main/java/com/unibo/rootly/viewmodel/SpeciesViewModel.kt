package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.Species
import com.unibo.rootly.data.repositories.SpeciesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpeciesViewModel @Inject constructor(
    private val repository: SpeciesRepository
): ViewModel() {

    fun insertSpecie(specie: Species) = viewModelScope.launch {
        repository.insert(specie)
    }

    fun getSpecieByName(name:String) = viewModelScope.launch {
        repository.getByName(name)
    }

    fun getAllSpeciesName() = repository.getAllSpeciesName()
}