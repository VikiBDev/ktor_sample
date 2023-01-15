package com.ktor_sample.modules.store.repository.mapper

import com.ktor_sample.modules.store.repository.dao.reward.RewardDao
import com.ktor_sample.modules.store.repository.dao.reward.type.CouponDao
import com.ktor_sample.modules.store.repository.dao.rewardmetadata.RewardMetadataDao
import com.ktor_sample.modules.store.repository.model.CouponEntity
import com.ktor_sample.modules.store.repository.model.GiveawayEntryEntity
import com.ktor_sample.modules.store.repository.model.RewardEntity
import org.jetbrains.exposed.sql.ResultRow

/**
 * Requires a right join between the reward table and the associated reward_metadata table row
 */
object RewardResultRowMapper {
    fun convert(row: ResultRow): RewardEntity =
        row.run {
            when (row[RewardMetadataDao.type]) {
                RewardMetadataDao.Type.COUPON -> CouponEntity(
                    row[RewardDao.id].value,
                    row[RewardDao.userId],
                    RewardMetadataResultRowMapper.convert(row),
                    row[CouponDao.code],
                )

                RewardMetadataDao.Type.GIVEAWAY -> GiveawayEntryEntity(
                    row[RewardDao.id].value,
                    row[RewardDao.userId],
                    RewardMetadataResultRowMapper.convert(row)
                )
            }
        }
}