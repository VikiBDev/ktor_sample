package com.ktor_sample.modules.store.repository.dao.rewardmetadata.type.base

import com.ktor_sample.modules.store.repository.dao.rewardmetadata.RewardMetadataDao
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insertAndGetId
import java.util.*

abstract class RewardMetadataTable(name: String) : Table(name) {
    /**
     * id is used only by E2E tests, should always be null in prod code
     */
    protected fun insertAndGetId(
        promotionId: UUID,
        price: Int,
        maxAmountPerUser: Int,
        totalAmount: Int,
        availableAmount: Int,
        type: RewardMetadataDao.Type,
        id: Long? = null
    ): Long {
        val insertedId = RewardMetadataDao.insertAndGetId {
            id?.let { id -> it[RewardMetadataDao.id] = id }
            it[RewardMetadataDao.promotionId] = promotionId
            it[RewardMetadataDao.price] = price
            it[RewardMetadataDao.maxAmountPerUser] = maxAmountPerUser
            it[RewardMetadataDao.totalAmount] = totalAmount
            it[RewardMetadataDao.availableAmount] = availableAmount
            it[RewardMetadataDao.type] = type
        }.value

        return insertedId
    }
}