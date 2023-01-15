package com.ktor_sample.modules.store.service.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.*

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = Coupon::class, name = "coupon"),
    JsonSubTypes.Type(value = GiveawayEntry::class, name = "giveaway_entry")
)
sealed class Reward {
    abstract val id: UUID
    abstract val rewardMetadata: RewardMetadata
}

data class Coupon(
    override val id: UUID,
    override val rewardMetadata: RewardMetadata,
    val code: String
) : Reward()

data class GiveawayEntry(
    override val id: UUID,
    override val rewardMetadata: RewardMetadata
) : Reward()
