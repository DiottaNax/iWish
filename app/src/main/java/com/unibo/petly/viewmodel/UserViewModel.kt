package com.unibo.petly.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.unibo.petly.R
import com.unibo.petly.data.database.Received
import com.unibo.petly.data.database.User
import com.unibo.petly.data.repositories.ReceivedRepository
import com.unibo.petly.data.repositories.UserRepository
import com.unibo.petly.utils.Notifications
import kotlinx.coroutines.flow.firstOrNull
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
        val user = getUserByName(username)
        return if(user != null && password == user.password){
            user.userId
        }else{
            -1
        }
    }

    fun setUser(userId: Int) {
        _user = userRepository.getUserById(userId)
    }

    fun setProfilePicture(uri: Uri) {
        _user?.let {
            val imgUri = userRepository.setProPic(it.userId, uri)
            _user = _user!!.copy(profileImg = imgUri.toString())
        }
    }

    suspend fun insertBadge(context: Context, name: String, userId: Int) {
        val badges = receivedRepository.getByUser(userId).firstOrNull() ?: emptyList()
        val badgeNames = badges.map { it.name }
        if (name !in badgeNames) {
            receivedRepository.insert(Received(name, userId))
            Notifications.sendNotification(
                name,
                context.getString(R.string.badge_notification_text),
                "Badge Received")
        }
    }
}