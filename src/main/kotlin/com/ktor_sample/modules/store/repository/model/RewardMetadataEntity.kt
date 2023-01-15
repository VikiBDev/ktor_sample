package com.ktor_sample.modules.store.repository.model

import java.time.Instant
import java.util.*

sealed class RewardMetadataEntity {
    abstract val id: Long
    abstract val promotionId: UUID
    abstract val price: Int
    abstract val maxAmountPerUser: Int
    abstract val totalAmount: Int
    abstract val availableAmount: Int
}

data class CouponMetadataEntity(
    override val id: Long,
    override val promotionId: UUID,
    override val price: Int,
    override val maxAmountPerUser: Int,
    override val totalAmount: Int,
    override val availableAmount: Int
) : RewardMetadataEntity()

data class GiveawayMetadataEntity(
    override val id: Long,
    override val promotionId: UUID,
    override val price: Int,
    override val maxAmountPerUser: Int,
    override val totalAmount: Int,
    override val availableAmount: Int,
    val startAt: Instant,
    val endAt: Instant
) : RewardMetadataEntity()
