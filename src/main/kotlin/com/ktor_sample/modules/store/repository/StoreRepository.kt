package com.ktor_sample.modules.store.repository

import com.ktor_sample.modules.store.repository.model.PromotionEntity
import com.ktor_sample.modules.store.repository.model.RewardEntity
import com.ktor_sample.modules.store.repository.model.TagEntity
import java.util.*

interface StoreRepository {
    suspend fun getPromotionsByTagId(tagId: Int): List<PromotionEntity>
    suspend fun getTagsByName(tags: List<String>): List<TagEntity>
    suspend fun getPromotion(promotionId: UUID): PromotionEntity
    suspend fun getPromotionForReward(rewardId: UUID): PromotionEntity
    suspend fun countRewardsForUser(rewardMetadataId: Long, userId: UUID): Long
    suspend fun availableRewardsAmount(rewardMetadataId: Long): Int
    suspend fun getReward(rewardId: UUID): RewardEntity
    suspend fun deleteReward(rewardId: UUID)
    suspend fun deleteRewardsForUser(userId: UUID)
    suspend fun addCouponToUser(code: String, userId: UUID, rewardMetadataId: Long): UUID
}