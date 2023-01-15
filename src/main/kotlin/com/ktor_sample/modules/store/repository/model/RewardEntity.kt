package com.ktor_sample.modules.store.repository.model

import java.util.*

sealed class RewardEntity {
    abstract val id: UUID
    abstract val userId: UUID
    abstract val rewardMetadata: RewardMetadataEntity
}

data class CouponEntity(
    override val id: UUID,
    override val userId: UUID,
    override val rewardMetadata: RewardMetadataEntity,
    val code: String
) : RewardEntity()

data class GiveawayEntryEntity(
    override val id: UUID,
    override val userId: UUID,
    override val rewardMetadata: RewardMetadataEntity
) : RewardEntity()
