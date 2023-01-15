package com.ktor_sample.modules.points.controller.mapper

import com.ktor_sample.foundation.Language
import com.ktor_sample.foundation.translate
import com.ktor_sample.modules.points.api.internal.model.StorePromotion
import com.ktor_sample.modules.points.controller.model.StorePromotionView

object StorePromotionMapper {
    fun convert(storePromotion: StorePromotion, language: Language): StorePromotionView =
        storePromotion.run {
            StorePromotionView(
                id,
                title.translate(language),
                imageUrl,
                partnerName
            )
        }
}