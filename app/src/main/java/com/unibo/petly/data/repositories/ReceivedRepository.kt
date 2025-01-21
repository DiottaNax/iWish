package com.unibo.petly.data.repositories

import com.unibo.petly.data.database.Received
import com.unibo.petly.data.database.daos.ReceivedDao

class ReceivedRepository (
    private val receivedDao: ReceivedDao
) {

    suspend fun insert(received: Received) = receivedDao.insertReceived(received)

    fun getByUser(userId: Int) = receivedDao.getReceivedByUser(userId)
}