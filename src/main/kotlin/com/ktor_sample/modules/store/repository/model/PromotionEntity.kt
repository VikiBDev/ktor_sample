package com.ktor_sample.modules.store.repository.model

import com.ktor_sample.foundation.LocalisedString
import java.util.*

data class PromotionEntity(
    val id: UUID,
    val categoryId: Int,
    val partnerId: Int,
    // UI info
    val localisedName: LocalisedString,
    val localisedDescription: LocalisedString,
    val imageUrl: String,
    val backgroundColor: String,
    val textColor: String,
    val categoryName: String,
    val localisedCategoryName: LocalisedString,
    // Partner info
    val partnerEntity: PartnerEntity,
    // Reward info
    val rewardMetadataEntity: RewardMetadataEntity
)
