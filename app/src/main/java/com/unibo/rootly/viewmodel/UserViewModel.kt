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

    private var _user: User? = null

    val user
        get() = _user

    suspend fun register(user: User) = userRepository.insert(user)

    private fun getUserByName(name: String) = userRepository.getUserByUsername(name)

    fun getReceivedBadgesByUser(userId: Int) = receivedRepository.getByUser(userId)

    fun login(username: String, password: String) : Int{
        val user = getUserByName(username);
        return if(user != null && password == user.password){
            user.userId
        }else{
            -1
        }
    }

    fun logout(){
        _user = null
    }

    fun setUser(userId: Int) {
        _user = userRepository.getUserById(userId)
    }
}