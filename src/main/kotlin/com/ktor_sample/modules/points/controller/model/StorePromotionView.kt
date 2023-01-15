package com.ktor_sample.modules.points.controller.model

import java.util.*

data class StorePromotionView(
    val id: UUID,
    val title: String,
    val imageUrl: String,
    val partnerName: String
)