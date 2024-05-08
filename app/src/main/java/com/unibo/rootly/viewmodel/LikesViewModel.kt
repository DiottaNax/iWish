package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.rootly.data.database.Likes
import com.unibo.rootly.data.repositories.LikesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikesViewModel @Inject constructor(
    private val repository: LikesRepository
): ViewModel() {

    fun insertLike(like: Likes) = viewModelScope.launch {
        repository.insert(like)
    }

}