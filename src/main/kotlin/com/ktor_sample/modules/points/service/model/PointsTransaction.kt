package com.ktor_sample.modules.points.service.model

import com.ktor_sample.modules.points.api.internal.model.StorePromotion
import com.ktor_sample.modules.points.api.internal.model.StoreReward
import java.time.Instant
import java.util.*

sealed class PointsTransaction {
    abstract val id: UUID
    abstract val amount: Int
    abstract val createdAt: Instant
}

data class StoreRewardDebit(
    override val id: UUID,
    override val amount: Int,
    override val createdAt: Instant,
    val storeReward: StoreReward,
    val storePromotion: StorePromotion,
) : PointsTransaction()

data class InvoicePaymentCredit(
    override val id: UUID,
    override val amount: Int,
    override val createdAt: Instant
) : PointsTransaction()
