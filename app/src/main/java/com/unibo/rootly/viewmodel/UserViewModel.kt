package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import com.unibo.rootly.data.database.User
import com.unibo.rootly.data.repositories.ReceivedRepository
import com.unibo.rootly.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val receivedRepository: ReceivedRepository
): ViewModel() {

    private var _user: User? = null

    val user
        get() = _user

    suspend fun register(user: User) :Boolean {
        val userId = userRepository.insert(user)
        return if( userId != -1L){
            _user = user.copy(userId = userId.toInt())
            true
        }else false
    }

    fun getUserByName(name: String) = userRepository.getUserByUsername(name)

    fun getReceivedBadgesByUser(userId: Int) = receivedRepository.getByUser(userId)

    fun login(username: String, password: String) : Boolean{
            val user = getUserByName(username);
        return if(user != null && password == user.password){
            _user = user
            true
        }else{
            false
        }
    }

    fun logout(){
        _user = null
    }
}