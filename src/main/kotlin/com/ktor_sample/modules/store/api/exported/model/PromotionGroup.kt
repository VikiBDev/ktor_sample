package com.ktor_sample.modules.store.service.model

import com.ktor_sample.foundation.LocalisedString

data class PromotionGroup(
    val id: String,
    val title: LocalisedString,
    val presentation: Presentation,
    val promotions: List<Promotion>
) {
    enum class Presentation {
        DEFAULT, LARGE
    }
}