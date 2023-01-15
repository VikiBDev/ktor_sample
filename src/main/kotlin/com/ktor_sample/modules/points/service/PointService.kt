package com.ktor_sample.modules.points.service

import com.ktor_sample.modules.points.service.model.PointBalance
import com.ktor_sample.modules.points.service.model.PointsTransaction
import com.ktor_sample.modules.points.service.model.PointsTransactionInfo
import java.util.*

interface PointService {
    suspend fun getBalance(userId: UUID): PointBalance
    suspend fun getTransaction(transactionId: UUID): PointsTransaction
    suspend fun addPoints(userId: UUID, amount: Int)
    suspend fun getTransactionsInfo(userId: UUID): List<PointsTransactionInfo>
}