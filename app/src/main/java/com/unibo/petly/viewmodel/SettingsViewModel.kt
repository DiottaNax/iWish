package com.unibo.petly.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.petly.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class Theme { Light, Dark, System }
data class ThemeState(val theme: Theme)

class SettingsViewModel (private val repository: SettingsRepository) : ViewModel() {
    var state by mutableStateOf(ThemeState(Theme.Light))
        private set

    fun setTheme(value: Theme) {
        state = ThemeState(value)
        viewModelScope.launch { repository.setTheme(value) }
    }

    init {
        viewModelScope.launch {
            state = ThemeState(repository.theme.first())
        }
    }
}
