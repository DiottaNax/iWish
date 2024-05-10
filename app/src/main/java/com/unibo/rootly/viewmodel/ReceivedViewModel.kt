package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.Received
import com.unibo.rootly.data.database.User
import com.unibo.rootly.data.repositories.ReceivedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceivedViewModel @Inject constructor(
    private val repository: ReceivedRepository
): ViewModel() {

    fun getReceivedBadgesByUser(userId: Int) = repository.getByUser(userId)

}