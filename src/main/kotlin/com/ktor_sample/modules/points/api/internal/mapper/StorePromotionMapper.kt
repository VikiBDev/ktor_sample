package com.ktor_sample.modules.points.api.internal.mapper

import com.ktor_sample.modules.points.api.internal.model.StorePromotion
import com.ktor_sample.modules.store.service.model.Promotion


object StorePromotionMapper {
    fun convert(promotion: Promotion): StorePromotion =
        promotion.run {
            StorePromotion(
                id,
                localisedName,
                imageUrl,
                promotion.partner.name
            )
        }
}