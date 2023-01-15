package com.ktor_sample.modules.store.repository.dao.rewardmetadata

import com.ktor_sample.foundation.database.customtable.TsLongIdTable
import com.ktor_sample.foundation.database.type.PostgresEnum
import com.ktor_sample.modules.store.repository.dao.PromotionDao

/**
 * This class contains all the columns shared by all rewards. The [com.ktor_sample.modules.store.repository.dao.rewardmetadata.type]
 * package contains all the DAOs that describe the specific properties of each reward.
 */
object RewardMetadataDao : TsLongIdTable("store.reward_metadata") {
    val promotionId = uuid("promotion_id").references(PromotionDao.id)
    val price = integer("price")
    val maxAmountPerUser = integer("max_amount_per_user")
    val totalAmount = integer("total_amount")
    val availableAmount = integer("available_amount")
    val type = RewardMetadataDao.customEnumeration(
        "type",
        "reward_type",
        { value -> Type.valueOf(value as String) },
        { PostgresEnum("reward_type", it) }).default(Type.COUPON)

    enum class Type {
        COUPON, GIVEAWAY
    }
}