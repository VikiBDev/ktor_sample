package com.ktor_sample.modules.store.exception

import com.ktor_sample.foundation.controller.exception.core.SilentException

object PromotionNotFoundException : SilentException(
    "store.error.promotion.not_found",
    "promotion_not_found"
)