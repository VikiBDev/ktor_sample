package com.ktor_sample.modules.points.service.impl

import com.ktor_sample.modules.points.repository.PointRepository
import com.ktor_sample.modules.points.service.PointService
import com.ktor_sample.modules.points.service.mapper.PointBalanceEntityMapper
import com.ktor_sample.modules.points.service.mapper.PointsTransactionEntityMapper
import com.ktor_sample.modules.points.service.mapper.PointsTransactionInfoEntityMapper
import com.ktor_sample.modules.points.service.model.PointBalance
import com.ktor_sample.modules.points.service.model.PointsTransaction
import com.ktor_sample.modules.points.service.model.PointsTransactionInfo
import java.util.*

class PointServiceImpl(private val pointRepository: PointRepository) : PointService {
    override suspend fun getBalance(userId: UUID): PointBalance {
        return pointRepository.getPointBalance(userId).let(PointBalanceEntityMapper::convert)
    }

    override suspend fun getTransaction(transactionId: UUID): PointsTransaction {
        return pointRepository.getPointsTransaction(transactionId).let(PointsTransactionEntityMapper::convert)
    }

    override suspend fun addPoints(userId: UUID, amount: Int) {
        pointRepository.changeBy(amount, userId)
    }

    override suspend fun getTransactionsInfo(userId: UUID): List<PointsTransactionInfo> {
        return pointRepository.getPointsTransactionsInfo(userId).map(PointsTransactionInfoEntityMapper::convert)
    }
}