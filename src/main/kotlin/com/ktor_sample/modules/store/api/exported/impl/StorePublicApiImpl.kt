package com.ktor_sample.modules.store.api.exported.impl

import com.ktor_sample.modules.store.api.exported.StorePublicApi
import com.ktor_sample.modules.store.service.model.Promotion
import com.ktor_sample.modules.store.service.model.Reward
import java.util.*

class StorePublicApiImpl: StorePublicApi {
    override suspend fun getReward(rewardId: UUID): Reward {
        TODO("Not yet implemented")
    }

    override suspend fun getPromotionForReward(rewardId: UUID): Promotion {
        TODO("Not yet implemented")
    }
}