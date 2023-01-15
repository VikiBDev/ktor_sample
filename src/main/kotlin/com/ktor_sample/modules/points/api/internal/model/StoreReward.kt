package com.ktor_sample.modules.points.api.internal.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant
import java.util.*

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = StoreCoupon::class, name = "coupon"),
    JsonSubTypes.Type(value = StoreGiveawayEntry::class, name = "giveaway")
)
sealed class StoreReward {
    abstract val id: UUID
    abstract val price: Int
}

data class StoreCoupon(
    override val id: UUID,
    override val price: Int,
    val code: String
) : StoreReward()

data class StoreGiveawayEntry(
    override val id: UUID,
    override val price: Int,
    val startAt: Instant,
    val endAt: Instant
) : StoreReward()
