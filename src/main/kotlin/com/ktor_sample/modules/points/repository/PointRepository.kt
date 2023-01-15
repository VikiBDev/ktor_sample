package com.ktor_sample.modules.points.repository

import com.ktor_sample.modules.points.repository.model.PointBalanceEntity
import com.ktor_sample.modules.points.repository.model.PointsTransactionEntity
import com.ktor_sample.modules.points.repository.model.PointsTransactionInfoEntity
import java.util.*

interface PointRepository {
    suspend fun getPointBalance(userId: UUID): PointBalanceEntity?
    suspend fun getPointsTransaction(transactionId: UUID): PointsTransactionEntity
    suspend fun getPointsTransactionsInfo(userId: UUID): List<PointsTransactionInfoEntity>
    suspend fun changeBy(amount: Int, userId: UUID)
    suspend fun deductPointsForRewardPurchase(amount: Int, rewardId: UUID, userId: UUID): UUID
    suspend fun removeTransaction(transactionId: UUID)
}