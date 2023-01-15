package com.ktor_sample.modules.store.api.exported.model

import java.time.Instant

sealed class StoreRewardMetadata {
    abstract val id: Long
    abstract val price: Int
    abstract val maxAmountPerUser: Int

    /** -1 means unlimited amount */
    abstract val totalAmount: Int

    // Ignore this property if totalAmount is -1
    abstract val availableAmount: Int
}

data class CouponMetadata(
    override val id: Long,
    override val price: Int,
    override val maxAmountPerUser: Int,
    override val totalAmount: Int,
    override val availableAmount: Int
) : StoreRewardMetadata()

data class GiveawayMetadata(
    override val id: Long,
    override val price: Int,
    override val maxAmountPerUser: Int,
    override val totalAmount: Int,
    override val availableAmount: Int,
    val startAt: Instant,
    val endAt: Instant
) : StoreRewardMetadata()