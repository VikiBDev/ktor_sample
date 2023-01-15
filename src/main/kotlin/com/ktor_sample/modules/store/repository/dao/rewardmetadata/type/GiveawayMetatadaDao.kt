package com.ktor_sample.modules.store.repository.dao.rewardmetadata.type

import com.ktor_sample.modules.store.repository.dao.rewardmetadata.RewardMetadataDao
import com.ktor_sample.modules.store.repository.dao.rewardmetadata.type.base.RewardMetadataTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.*

object GiveawayMetatadaDao : RewardMetadataTable("store.giveaway_metadata") {
    val rewardMetadataId = long("reward_metadata_id").references(RewardMetadataDao.id)
    val startAt = timestamp("start_at")
    val endAt = timestamp("end_at")

    fun insertAndGetId(
        promotionId: UUID,
        price: Int,
        maxAmountPerUser: Int,
        totalAmount: Int,
        availableAmount: Int,
        startAt: Instant,
        endAt: Instant,
        id: Long? = null
    ): Long {
        val rewardMetadataId = insertAndGetId(
            promotionId,
            price,
            maxAmountPerUser,
            totalAmount,
            availableAmount,
            RewardMetadataDao.Type.GIVEAWAY,
            id
        )

        insert {
            it[GiveawayMetatadaDao.rewardMetadataId] = rewardMetadataId
            it[GiveawayMetatadaDao.startAt] = startAt
            it[GiveawayMetatadaDao.endAt] = endAt
        }

        return rewardMetadataId
    }
}