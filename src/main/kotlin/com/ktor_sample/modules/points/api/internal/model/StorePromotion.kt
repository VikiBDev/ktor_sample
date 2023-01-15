package com.ktor_sample.modules.points.api.internal.model

import com.ktor_sample.foundation.LocalisedString
import java.util.*

data class StorePromotion(
    val id: UUID,
    val title: LocalisedString,
    val imageUrl: String,
    val partnerName: String
)