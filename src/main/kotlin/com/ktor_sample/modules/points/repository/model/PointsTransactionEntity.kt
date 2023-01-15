package com.ktor_sample.modules.points.repository.model

import com.ktor_sample.modules.points.api.internal.model.StorePromotion
import com.ktor_sample.modules.points.api.internal.model.StoreReward
import java.time.Instant
import java.util.*

sealed class PointsTransactionEntity {
    abstract val id: UUID
    abstract val amount: Int
    abstract val createdAt: Instant
}

data class StoreRewardTransactionEntity(
    override val id: UUID,
    override val amount: Int,
    override val createdAt: Instant
) : PointsTransactionEntity() {
    lateinit var storeReward: StoreReward
    lateinit var storePromotion: StorePromotion
}

data class InvoicePaymentTransactionEntity(
    override val id: UUID,
    override val amount: Int,
    override val createdAt: Instant
) : PointsTransactionEntity()

