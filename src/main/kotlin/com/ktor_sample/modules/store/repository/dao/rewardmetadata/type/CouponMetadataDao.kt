package com.ktor_sample.modules.store.repository.dao.rewardmetadata.type

import com.ktor_sample.modules.store.repository.dao.rewardmetadata.RewardMetadataDao
import com.ktor_sample.modules.store.repository.dao.rewardmetadata.type.base.RewardMetadataTable
import org.jetbrains.exposed.sql.insert
import java.util.*


object CouponMetadataDao : RewardMetadataTable("store.coupon_metadata") {
    val rewardMetadataId = long("reward_metadata_id").references(RewardMetadataDao.id)

    fun insertAndGetId(
        promotionId: UUID,
        price: Int,
        maxAmountPerUser: Int,
        totalAmount: Int,
        availableAmount: Int,
        id: Long? = null
    ): Long {
        val rewardMetadataId = insertAndGetId(
            promotionId,
            price,
            maxAmountPerUser,
            totalAmount,
            availableAmount,
            RewardMetadataDao.Type.COUPON,
            id
        )

        insert {
            it[CouponMetadataDao.rewardMetadataId] = rewardMetadataId
        }

        return rewardMetadataId
    }
}