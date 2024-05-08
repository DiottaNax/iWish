package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Received
import com.unibo.rootly.data.database.daos.ReceivedDao

class ReceivedRepository(
    private val receivedDao: ReceivedDao
) {

    suspend fun insert(received: Received) = receivedDao.insertReceived(received)

    suspend fun getByUser(userId: Int) = receivedDao.getReceivedByUser(userId)
}