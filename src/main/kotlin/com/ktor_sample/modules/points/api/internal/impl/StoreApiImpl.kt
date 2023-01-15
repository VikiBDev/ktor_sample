package com.ktor_sample.modules.points.api.internal.impl

import com.ktor_sample.modules.points.api.internal.StoreApi
import com.ktor_sample.modules.points.api.internal.mapper.StorePromotionMapper
import com.ktor_sample.modules.points.api.internal.mapper.StoreRewardMapper
import com.ktor_sample.modules.points.api.internal.model.StorePromotion
import com.ktor_sample.modules.points.api.internal.model.StoreReward
import com.ktor_sample.modules.store.api.exported.StorePublicApi
import java.util.*

class StoreApiImpl(private val storePublicApi: StorePublicApi) : StoreApi {
    override suspend fun getReward(rewardId: UUID): StoreReward {
        return storePublicApi.getReward(rewardId).let(StoreRewardMapper::convert)
    }

    override suspend fun getPromotionForReward(rewardId: UUID): StorePromotion {
        return storePublicApi.getPromotionForReward(rewardId).let(StorePromotionMapper::convert)
    }
}