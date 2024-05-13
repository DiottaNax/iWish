package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.BadgeType
import com.unibo.rootly.data.database.daos.BadgeTypeDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BadgeTypeRepository @Inject constructor(
    private val badgeTypeDao: BadgeTypeDao
) {
    val badgeTypes : Flow<List<BadgeType>> = badgeTypeDao.getAllBadgeTypes()
}
