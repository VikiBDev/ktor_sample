package com.ktor_sample.modules.points.service.mapper

import com.ktor_sample.modules.points.repository.model.InvoicePaymentTransactionEntity
import com.ktor_sample.modules.points.repository.model.PointsTransactionEntity
import com.ktor_sample.modules.points.repository.model.StoreRewardTransactionEntity
import com.ktor_sample.modules.points.service.model.InvoicePaymentCredit
import com.ktor_sample.modules.points.service.model.PointsTransaction
import com.ktor_sample.modules.points.service.model.StoreRewardDebit

object PointsTransactionEntityMapper {
    fun convert(entity: PointsTransactionEntity): PointsTransaction =
        entity.run {
            when (entity) {
                is StoreRewardTransactionEntity -> {
                    StoreRewardDebit(
                        entity.id,
                        entity.amount,
                        entity.createdAt,
                        entity.storeReward,
                        entity.storePromotion
                    )
                }

                is InvoicePaymentTransactionEntity ->
                    InvoicePaymentCredit(
                        entity.id,
                        entity.amount,
                        entity.createdAt
                    )
            }
        }
}