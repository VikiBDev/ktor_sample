package com.ktor_sample.modules.points.api.internal

import com.ktor_sample.modules.points.api.internal.model.StorePromotion
import com.ktor_sample.modules.points.api.internal.model.StoreReward
import java.util.*

interface StoreApi {
    suspend fun getReward(rewardId: UUID): StoreReward
    suspend fun getPromotionForReward(rewardId: UUID): StorePromotion
}