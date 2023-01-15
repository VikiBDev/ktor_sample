package com.ktor_sample.modules.store.api.exported

import com.ktor_sample.modules.store.service.model.Promotion
import com.ktor_sample.modules.store.service.model.Reward
import java.util.*

interface StorePublicApi {
    suspend fun getReward(rewardId: UUID): Reward
    suspend fun getPromotionForReward(rewardId: UUID): Promotion
}