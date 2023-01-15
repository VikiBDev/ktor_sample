package com.ktor_sample.modules.points.repository.dao.transaction.type

import com.ktor_sample.modules.points.repository.dao.transaction.PointsTransactionDao
import com.ktor_sample.modules.points.repository.dao.transaction.type.base.PointsTransactionTable
import org.jetbrains.exposed.sql.insert
import java.util.*

// TODO Replace storeReward values with invoice payment when that model is ready
object InvoicePaymentTransactionDao : PointsTransactionTable("point.invoice_payment_transaction") {
    val pointsTransactionId = uuid("points_transaction_id").references(PointsTransactionDao.id)

    fun insertAndGetId(
        pointBalanceId: Long,
        amount: Int,
        id: UUID? = null
    ): UUID {
        val pointTransactionId = insertAndGetId(
            pointBalanceId,
            amount,
            PointsTransactionDao.Type.INVOICE_PAYMENT,
            id
        )

        insert {
            it[InvoicePaymentTransactionDao.pointsTransactionId] = pointTransactionId
        }

        return pointTransactionId
    }
}
