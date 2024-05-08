package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.BadgeType
import com.unibo.rootly.data.database.daos.BadgeTypeDao
import kotlinx.coroutines.flow.Flow

class BadgeTypeRepository constructor(
    private val badgeTypeDao: BadgeTypeDao
) {
    suspend fun getAllTypes() : Flow<List<BadgeType>> = badgeTypeDao.getAllBadgeTypes()
}
