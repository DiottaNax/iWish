package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.User
import com.unibo.rootly.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    fun insertLUser(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    fun getUserByName(name: String) = repository.getUserByUsername(name)

}