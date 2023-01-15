package com.ktor_sample.modules.points.controller.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ktor_sample.modules.points.api.internal.model.StoreReward
import java.time.Instant
import java.util.*

/**
 * Unfortunately we need a wrapper because Jackson doesn't play well with polymorphic values assigned to a
 * [Result.Success], since its value is a generic type.
 */
data class PointsTransactionWrapperView(val transaction: PointsTransactionView)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = StoreRewardDebitView::class, name = "store_reward"),
    JsonSubTypes.Type(value = InvoicePaymentCreditView::class, name = "invoice_payment")
)
sealed class PointsTransactionView {
    abstract val id: UUID
    abstract val amount: Int
    abstract val createdAt: Instant
}

data class StoreRewardDebitView(
    override val id: UUID,
    override val amount: Int,
    override val createdAt: Instant,
    val storeReward: StoreReward,
    val storePromotion: StorePromotionView
) : PointsTransactionView()

data class InvoicePaymentCreditView(
    override val id: UUID,
    override val amount: Int,
    override val createdAt: Instant
) : PointsTransactionView()
