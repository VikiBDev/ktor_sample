package com.ktor_sample.modules.points.repository.impl

import com.ktor_sample.modules.points.api.internal.StoreApi
import com.ktor_sample.modules.points.exception.NotEnoughPointsException
import com.ktor_sample.modules.points.repository.PointRepository
import com.ktor_sample.modules.points.repository.dao.PointBalanceDao
import com.ktor_sample.modules.points.repository.dao.transaction.PointsTransactionDao
import com.ktor_sample.modules.points.repository.dao.transaction.type.InvoicePaymentTransactionDao
import com.ktor_sample.modules.points.repository.dao.transaction.type.StoreRewardTransactionDao
import com.ktor_sample.modules.points.repository.mapper.PointBalanceResultRowMapper
import com.ktor_sample.modules.points.repository.mapper.PointsTransactionInfoResultRowMapper
import com.ktor_sample.modules.points.repository.mapper.PointsTransactionResultRowMapper
import com.ktor_sample.modules.points.repository.model.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import java.util.*


class PointRepositoryImpl(private val database: Database, private val storeApi: StoreApi) : PointRepository {
    override suspend fun getPointBalance(userId: UUID): PointBalanceEntity? {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            val pointBalance = PointBalanceDao
                .select(PointBalanceDao.userId eq userId)
                .singleOrNull()
                ?.let(PointBalanceResultRowMapper::convert)

            if (pointBalance != null) {
                PointBalanceDao.update({ PointBalanceDao.userId eq userId }) {
                    it[PointBalanceDao.seenAt] = Instant.now()
                    it[PointBalanceDao.lastSeenBalance] = pointBalance.amount
                }
            }

            pointBalance
        }
    }

    override suspend fun getPointsTransaction(transactionId: UUID): PointsTransactionEntity {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            val resultRow = PointsTransactionDao
                .leftJoin(StoreRewardTransactionDao)
                .leftJoin(InvoicePaymentTransactionDao)
                .select(PointsTransactionDao.id eq transactionId)
                .single()

            val entity = resultRow.let(PointsTransactionResultRowMapper::convert)

            when (entity) {
                is StoreRewardTransactionEntity -> {
                    entity.storeReward =
                        storeApi.getReward(resultRow[StoreRewardTransactionDao.storeRewardId])
                    entity.storePromotion =
                        storeApi.getPromotionForReward(resultRow[StoreRewardTransactionDao.storeRewardId])
                }

                is InvoicePaymentTransactionEntity -> TODO()
            }

            return@newSuspendedTransaction entity
        }
    }

    override suspend fun getPointsTransactionsInfo(userId: UUID): List<PointsTransactionInfoEntity> {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            val pointsBalanceId = PointBalanceDao
                .slice(PointBalanceDao.id)
                .select(PointBalanceDao.userId eq userId)
                .singleOrNull()
                ?.get(PointBalanceDao.id)?.value
                ?: return@newSuspendedTransaction listOf()

            val resultRow = PointsTransactionDao
                .select(PointsTransactionDao.pointBalanceId eq pointsBalanceId)
                .map(PointsTransactionInfoResultRowMapper::convert)

            resultRow
        }
    }

    override suspend fun changeBy(amount: Int, userId: UUID) {
        updateBalanceAnd(amount, userId) { }
    }

    override suspend fun deductPointsForRewardPurchase(amount: Int, rewardId: UUID, userId: UUID): UUID {
        return updateBalanceAnd(-amount, userId) { pointBalanceId ->
            StoreRewardTransactionDao.insertAndGetId(pointBalanceId, rewardId, -amount)
        }
    }

    override suspend fun removeTransaction(transactionId: UUID) {
        PointsTransactionDao.deleteWhere { PointsTransactionDao.id eq transactionId }
    }

    private suspend fun <T> updateBalanceAnd(amount: Int, userId: UUID, block: (Long) -> T): T {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            var pointBalanceId: Long? = null
            val pointBalance = PointBalanceDao
                .select(PointBalanceDao.userId eq userId)
                .singleOrNull()

            if (pointBalance != null) {
                pointBalanceId = pointBalance[PointBalanceDao.id].value
                if (pointBalance[PointBalanceDao.amount] + amount < 0)
                    throw NotEnoughPointsException

                PointBalanceDao.update({ PointBalanceDao.id eq pointBalance[PointBalanceDao.id] }) {
                    with(SqlExpressionBuilder) {
                        /**
                         *  If, before this update, the client had the up-to-date balance, then we store the pre-change
                         *  balance so that we can later send it to the client whenever it fetches the balance
                         */
                        if (pointBalance[PointBalanceDao.seenAt] >= pointBalance[PointBalanceDao.changedAt])
                            it[PointBalanceDao.lastSeenBalance] = PointBalanceDao.amount

                        it[PointBalanceDao.amount] = PointBalanceDao.amount + amount
                        it[PointBalanceDao.changedAt] = Instant.now()
                    }
                }
            } else {
                if (amount < 0)
                    throw NotEnoughPointsException

                pointBalanceId = PointBalanceDao.insertAndGetId {
                    it[PointBalanceDao.amount] = amount
                    it[PointBalanceDao.lastSeenBalance] = 0
                    it[PointBalanceDao.userId] = userId
                    /**
                     * When a balance is created for the first time, we want the changedAt field to be greater than
                     * the seen_at flag, so that the client is notified of the fact that the balance has changed
                     */
                    it[PointBalanceDao.changedAt] = Instant.now().plusMillis(1)
                }.value
            }
            return@newSuspendedTransaction block(pointBalanceId)
        }
    }

}