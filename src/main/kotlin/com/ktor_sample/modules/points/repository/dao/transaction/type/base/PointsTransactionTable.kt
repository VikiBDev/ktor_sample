package com.ktor_sample.modules.points.repository.dao.transaction.type.base

import com.ktor_sample.modules.points.repository.dao.transaction.PointsTransactionDao
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insertAndGetId
import java.util.*

abstract class PointsTransactionTable(name: String) : Table(name) {
    /**
     * id is used only by E2E tests, should always be null in prod code
     */
    protected fun insertAndGetId(
        pointBalanceId: Long,
        amount: Int,
        type: PointsTransactionDao.Type,
        id: UUID? = null
    ): UUID {
        val insertedId = PointsTransactionDao.insertAndGetId {
            id?.let { id -> it[PointsTransactionDao.id] = id }
            it[PointsTransactionDao.pointBalanceId] = pointBalanceId
            it[PointsTransactionDao.amount] = amount
            it[PointsTransactionDao.type] = type
        }.value

        return insertedId
    }
}