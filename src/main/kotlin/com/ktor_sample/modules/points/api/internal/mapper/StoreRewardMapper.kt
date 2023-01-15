package com.ktor_sample.modules.points.api.internal.mapper

import com.ktor_sample.modules.points.api.internal.model.StoreCoupon
import com.ktor_sample.modules.points.api.internal.model.StoreGiveawayEntry
import com.ktor_sample.modules.points.api.internal.model.StoreReward
import com.ktor_sample.modules.store.service.model.Coupon
import com.ktor_sample.modules.store.service.model.GiveawayEntry
import com.ktor_sample.modules.store.service.model.GiveawayMetadata
import com.ktor_sample.modules.store.service.model.Reward


object StoreRewardMapper {
    fun convert(reward: Reward): StoreReward =
        reward.run {
            when (reward) {
                is Coupon -> StoreCoupon(
                    reward.id,
                    reward.rewardMetadata.price,
                    reward.code
                )

                is GiveawayEntry -> StoreGiveawayEntry(
                    reward.id,
                    reward.rewardMetadata.price,
                    (reward.rewardMetadata as GiveawayMetadata).startAt,
                    (reward.rewardMetadata as GiveawayMetadata).endAt,
                )
            }
        }
}