package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.User
import com.unibo.rootly.data.repositories.ReceivedRepository
import com.unibo.rootly.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val receivedRepository: ReceivedRepository
): ViewModel() {

    fun insertLUser(user: User) = viewModelScope.launch {
        userRepository.insert(user)
    }

    fun getUserByName(name: String) = userRepository.getUserByUsername(name)

    fun getReceivedBadgesByUser(userId: Int) = receivedRepository.getByUser(userId)

}