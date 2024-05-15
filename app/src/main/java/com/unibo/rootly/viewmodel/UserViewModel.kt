package com.unibo.rootly.viewmodel

import androidx.lifecycle.ViewModel
import com.unibo.rootly.data.database.User
import com.unibo.rootly.data.repositories.ReceivedRepository
import com.unibo.rootly.data.repositories.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class UserViewModel : ViewModel(), KoinComponent {
    private val userRepository: UserRepository by inject()
    private val receivedRepository: ReceivedRepository by inject()

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