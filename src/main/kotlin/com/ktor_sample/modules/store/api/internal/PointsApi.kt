package com.ktor_sample.modules.store.api.internal

import java.util.*

interface PointsApi {
    suspend fun deductPointsForRewardPurchase(amount: Int, rewardId: UUID, userId: UUID): UUID
    suspend fun removeTransaction(transactionId: UUID)
}