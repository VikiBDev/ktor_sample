package com.ktor_sample.modules.points.controller.mapper

import com.ktor_sample.foundation.Language
import com.ktor_sample.modules.points.controller.model.PointsTransactionWrapperView
import com.ktor_sample.modules.points.controller.model.StoreRewardDebitView
import com.ktor_sample.modules.points.service.model.InvoicePaymentCredit
import com.ktor_sample.modules.points.service.model.PointsTransaction
import com.ktor_sample.modules.points.service.model.StoreRewardDebit

object PointsTransactionMapper {
    fun convert(pointsTransaction: PointsTransaction, language: Language): PointsTransactionWrapperView =
        pointsTransaction.run {
            PointsTransactionWrapperView(
                when (this) {
                    is StoreRewardDebit -> {
                        StoreRewardDebitView(
                            id,
                            amount,
                            createdAt,
                            storeReward,
                            storePromotion.let { StorePromotionMapper.convert(it, language) }
                        )
                    }

                    is InvoicePaymentCredit -> TODO()
                }
            )
        }
}