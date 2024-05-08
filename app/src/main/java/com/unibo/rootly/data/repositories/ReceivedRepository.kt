package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Received
import com.unibo.rootly.data.database.daos.ReceivedDao
import javax.inject.Inject

class ReceivedRepository @Inject constructor(
    private val receivedDao: ReceivedDao
) {

    @WorkerThread
    suspend fun insert(received: Received) = receivedDao.insertReceived(received)

    @WorkerThread
    suspend fun getByUser(userId: Int) = receivedDao.getReceivedByUser(userId)
}