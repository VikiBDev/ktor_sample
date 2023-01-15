package com.ktor_sample.modules.store.repository.dao.reward.type

import com.ktor_sample.modules.store.repository.dao.reward.RewardDao
import com.ktor_sample.modules.store.repository.dao.reward.type.base.RewardTable
import org.jetbrains.exposed.sql.insert
import java.util.*

object CouponDao : RewardTable("store.coupon") {
    val rewardId = uuid("reward_id").references(RewardDao.id)
    val code = varchar("code", 128)

    fun insertAndGetId(code: String, userId: UUID, rewardMetadataId: Long): UUID {
        val rewardId = insertAndGetId(userId, rewardMetadataId)
        insert {
            it[CouponDao.rewardId] = rewardId
            it[CouponDao.code] = code
        }

        return rewardId
    }
}
