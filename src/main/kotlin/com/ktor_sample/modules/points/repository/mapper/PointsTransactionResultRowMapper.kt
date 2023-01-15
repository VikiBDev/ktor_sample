package com.ktor_sample.modules.points.repository.mapper

import com.ktor_sample.modules.points.repository.dao.transaction.PointsTransactionDao
import com.ktor_sample.modules.points.repository.model.InvoicePaymentTransactionEntity
import com.ktor_sample.modules.points.repository.model.PointsTransactionEntity
import com.ktor_sample.modules.points.repository.model.StoreRewardTransactionEntity
import org.jetbrains.exposed.sql.ResultRow

object PointsTransactionResultRowMapper {
    fun convert(row: ResultRow): PointsTransactionEntity =
        row.run {
            when (row[PointsTransactionDao.type]) {
                PointsTransactionDao.Type.STORE_REWARD_PURCHASE -> StoreRewardTransactionEntity(
                    this[PointsTransactionDao.id].value,
                    this[PointsTransactionDao.amount],
                    this[PointsTransactionDao.createdAt]
                )

                PointsTransactionDao.Type.INVOICE_PAYMENT -> InvoicePaymentTransactionEntity(
                    this[PointsTransactionDao.id].value,
                    this[PointsTransactionDao.amount],
                    this[PointsTransactionDao.createdAt]
                )
            }
        }
}