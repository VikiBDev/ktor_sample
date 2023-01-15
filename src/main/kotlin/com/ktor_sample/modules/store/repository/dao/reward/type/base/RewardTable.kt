package com.ktor_sample.modules.store.repository.dao.reward.type.base

import com.ktor_sample.modules.store.repository.dao.reward.RewardDao
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insertAndGetId
import java.util.*

abstract class RewardTable(name: String) : Table(name) {
    /**
     * id is used only by E2E tests, should always be null in prod code
     */
    protected fun insertAndGetId(userId: UUID, rewardMetadataId: Long, id: UUID? = null): UUID {
        val insertedId = RewardDao.insertAndGetId {
            id?.let { id -> it[RewardDao.id] = id }
            it[RewardDao.userId] = userId
            it[RewardDao.rewardMetadataId] = rewardMetadataId
        }.value

        return insertedId
    }
}