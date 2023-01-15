package com.ktor_sample.modules.store.repository.mapper

import com.ktor_sample.modules.store.repository.dao.rewardmetadata.RewardMetadataDao
import com.ktor_sample.modules.store.repository.dao.rewardmetadata.type.GiveawayMetatadaDao
import com.ktor_sample.modules.store.repository.model.CouponMetadataEntity
import com.ktor_sample.modules.store.repository.model.GiveawayMetadataEntity
import com.ktor_sample.modules.store.repository.model.RewardMetadataEntity
import org.jetbrains.exposed.sql.ResultRow

object RewardMetadataResultRowMapper {
    fun convert(row: ResultRow): RewardMetadataEntity =
        row.run {
            when (row[RewardMetadataDao.type]) {
                RewardMetadataDao.Type.COUPON -> CouponMetadataEntity(
                    this[RewardMetadataDao.id].value,
                    this[RewardMetadataDao.promotionId],
                    this[RewardMetadataDao.price],
                    this[RewardMetadataDao.maxAmountPerUser],
                    this[RewardMetadataDao.totalAmount],
                    this[RewardMetadataDao.availableAmount]
                )

                RewardMetadataDao.Type.GIVEAWAY -> GiveawayMetadataEntity(
                    this[RewardMetadataDao.id].value,
                    this[RewardMetadataDao.promotionId],
                    this[RewardMetadataDao.price],
                    this[RewardMetadataDao.maxAmountPerUser],
                    this[RewardMetadataDao.totalAmount],
                    this[RewardMetadataDao.availableAmount],
                    this[GiveawayMetatadaDao.startAt],
                    this[GiveawayMetatadaDao.endAt]
                )
            }
        }
}