package com.ktor_sample.modules.store.service.model

import com.ktor_sample.foundation.LocalisedString
import java.util.*

data class Promotion(
    val id: UUID,
    val localisedName: LocalisedString,
    val localisedDescription: LocalisedString,
    val imageUrl: String,
    val textColor: String,
    val backgroundColor: String,
    val partner: Partner,
    val rewardMetadata: RewardMetadata
)