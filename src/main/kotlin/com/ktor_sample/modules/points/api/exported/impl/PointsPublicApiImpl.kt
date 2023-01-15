package com.ktor_sample.modules.points.api.exported.impl

import com.ktor_sample.modules.points.api.exported.PointsPublicApi
import com.ktor_sample.modules.points.repository.PointRepository
import java.util.*

class PointsPublicApiImpl(private val pointRepository: PointRepository) : PointsPublicApi {
    override suspend fun deductPointsForRewardPurchase(amount: Int, rewardId: UUID, userId: UUID): UUID {
        return pointRepository.deductPointsForRewardPurchase(amount, rewardId, userId)
    }

    override suspend fun removeTransaction(transactionId: UUID) {
        pointRepository.removeTransaction(transactionId)
    }
}