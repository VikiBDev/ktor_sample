package com.ktor_sample.modules.store.api.internal.impl

import com.ktor_sample.modules.points.api.exported.PointsPublicApi
import com.ktor_sample.modules.store.api.internal.PointsApi
import java.util.*

class PointsApiImpl(private val pointsPublicApi: PointsPublicApi) : PointsApi {
    override suspend fun deductPointsForRewardPurchase(amount: Int, rewardId: UUID, userId: UUID): UUID {
        return pointsPublicApi.deductPointsForRewardPurchase(amount, rewardId, userId)
    }

    override suspend fun removeTransaction(transactionId: UUID) {
        pointsPublicApi.removeTransaction(transactionId)
    }
}