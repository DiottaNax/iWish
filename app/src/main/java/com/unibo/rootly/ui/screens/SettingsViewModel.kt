package com.unibo.rootly.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Theme { Light, Dark, System }
data class ThemeState(val theme: Theme)

class SettingsViewModel () : ViewModel() {
    private val _state = MutableStateFlow(ThemeState(Theme.System))
    val state = _state.asStateFlow()

    fun changeTheme(theme: Theme) {
        _state.value = ThemeState(theme)
    }
}