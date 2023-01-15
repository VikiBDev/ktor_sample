package com.ktor_sample.modules.points.api.exported

import java.util.*

interface PointsPublicApi {
    suspend fun deductPointsForRewardPurchase(amount: Int, rewardId: UUID, userId: UUID): UUID
    suspend fun removeTransaction(transactionId: UUID)
}