package com.ktor_sample.modules.store.repository.impl

import com.ktor_sample.modules.points.repository.dao.PointBalanceDao
import com.ktor_sample.modules.points.repository.dao.transaction.PointsTransactionDao
import com.ktor_sample.modules.points.repository.dao.transaction.type.StoreRewardTransactionDao
import com.ktor_sample.modules.store.exception.PromotionNotFoundException
import com.ktor_sample.modules.store.repository.StoreRepository
import com.ktor_sample.modules.store.repository.dao.*
import com.ktor_sample.modules.store.repository.dao.reward.RewardDao
import com.ktor_sample.modules.store.repository.dao.reward.type.CouponDao
import com.ktor_sample.modules.store.repository.dao.reward.type.GiveawayEntryDao
import com.ktor_sample.modules.store.repository.dao.rewardmetadata.RewardMetadataDao
import com.ktor_sample.modules.store.repository.dao.rewardmetadata.type.CouponMetadataDao
import com.ktor_sample.modules.store.repository.dao.rewardmetadata.type.GiveawayMetatadaDao
import com.ktor_sample.modules.store.repository.mapper.PromotionResultRowMapper
import com.ktor_sample.modules.store.repository.mapper.RewardResultRowMapper
import com.ktor_sample.modules.store.repository.mapper.TagResultRowMapper
import com.ktor_sample.modules.store.repository.model.PromotionEntity
import com.ktor_sample.modules.store.repository.model.RewardEntity
import com.ktor_sample.modules.store.repository.model.TagEntity
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.minus
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class StoreRepositoryImpl(private val database: Database) : StoreRepository {
    override suspend fun getPromotionsByTagId(tagId: Int): List<PromotionEntity> {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            PromotionTagDao
                .rightJoin(PromotionDao)
                .rightJoin(CategoryDao)
                .rightJoin(PartnerDao)
                .rightJoin(RewardMetadataDao)
                .leftJoin(CouponMetadataDao)
                .leftJoin(GiveawayMetatadaDao)
                .select(PromotionTagDao.tagId.eq(tagId))
                .map(PromotionResultRowMapper::convert)
        }
    }

    override suspend fun getTagsByName(tags: List<String>): List<TagEntity> {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            TagDao.select(TagDao.name.inList(tags)).map(TagResultRowMapper::convert)
        }
    }

    /**
     * @throws PromotionNotFoundException
     */
    override suspend fun getPromotion(promotionId: UUID): PromotionEntity {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            try {
                PromotionDao
                    .rightJoin(CategoryDao)
                    .rightJoin(PartnerDao)
                    .rightJoin(RewardMetadataDao)
                    .leftJoin(CouponMetadataDao)
                    .leftJoin(GiveawayMetatadaDao)
                    .select(PromotionDao.id eq promotionId)
                    .single()
                    .let(PromotionResultRowMapper::convert)
            } catch (e: NoSuchElementException) {
                throw PromotionNotFoundException
            }
        }
    }

    override suspend fun getPromotionForReward(rewardId: UUID): PromotionEntity {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            try {
                val promotionId = RewardDao
                    .leftJoin(RewardMetadataDao)
                    .slice(RewardMetadataDao.promotionId)
                    .select(RewardDao.id eq rewardId)
                    .single()[RewardMetadataDao.promotionId]

                PromotionDao
                    .rightJoin(CategoryDao)
                    .rightJoin(PartnerDao)
                    .rightJoin(RewardMetadataDao)
                    .leftJoin(CouponMetadataDao)
                    .leftJoin(GiveawayMetatadaDao)
                    .select(PromotionDao.id eq promotionId)
                    .single()
                    .let(PromotionResultRowMapper::convert)
            } catch (e: NoSuchElementException) {
                throw PromotionNotFoundException
            }
        }
    }

    override suspend fun countRewardsForUser(rewardMetadataId: Long, userId: UUID): Long {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            RewardDao.select(RewardDao.userId.eq(userId) and RewardDao.rewardMetadataId.eq(rewardMetadataId)).count()
        }
    }

    override suspend fun availableRewardsAmount(rewardMetadataId: Long): Int {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            RewardMetadataDao
                .slice(RewardMetadataDao.availableAmount)
                .select(RewardMetadataDao.id eq rewardMetadataId)
                .single()[RewardMetadataDao.availableAmount]
        }
    }

    override suspend fun getReward(rewardId: UUID): RewardEntity {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            RewardDao
                .rightJoin(RewardMetadataDao)
                .leftJoin(CouponDao)
                .leftJoin(GiveawayEntryDao)
                .select(RewardDao.id eq rewardId)
                .single()
                .let(RewardResultRowMapper::convert)
        }
    }

    override suspend fun deleteReward(rewardId: UUID) {
        newSuspendedTransaction(Dispatchers.IO, database) {
            val rewardMetadataId = RewardDao
                .slice(RewardDao.rewardMetadataId)
                .select(RewardDao.id eq rewardId)
                .single()[RewardDao.rewardMetadataId]

            RewardMetadataDao
                .update({ RewardMetadataDao.id eq rewardMetadataId }) {
                    it[RewardMetadataDao.availableAmount] = RewardMetadataDao.availableAmount + 1
                }

            RewardDao.deleteWhere { RewardDao.id.eq(rewardId) }
        }
    }

    /**
     * Lazy implementation, depends directly on models from the points module without using the public api
     * workflow. This is only a debug endpoint that's supposed to disappear at some point, but if we want to keep it
     * long term then we'll have to refactor to use a proper architecture
     */
    override suspend fun deleteRewardsForUser(userId: UUID) {
        newSuspendedTransaction(Dispatchers.IO, database) {
            RewardDao.slice(RewardDao.id, RewardDao.rewardMetadataId).select(RewardDao.userId eq userId).map { reward ->
                StoreRewardTransactionDao
                    .select(StoreRewardTransactionDao.storeRewardId eq reward[RewardDao.id].value)
                    .map { storeReward ->
                        // Delete transaction and restore balance
                        val transactionId = storeReward[StoreRewardTransactionDao.pointsTransactionId]
                        val transaction = PointsTransactionDao.select(PointsTransactionDao.id eq transactionId).single()
                        PointBalanceDao.update({ PointBalanceDao.userId eq userId }) {
                            it[PointBalanceDao.amount] =
                                PointBalanceDao.amount + transaction[PointsTransactionDao.amount]
                        }
                        PointsTransactionDao.deleteWhere { PointsTransactionDao.id eq transactionId }
                        // Restore reward metadata available amount
                        RewardMetadataDao.update({ RewardMetadataDao.id eq reward[RewardDao.rewardMetadataId] }) {
                            it[RewardMetadataDao.availableAmount] = RewardMetadataDao.availableAmount + 1
                        }
                    }
            }
            RewardDao.deleteWhere { RewardDao.userId eq userId }
        }
    }

    override suspend fun addCouponToUser(code: String, userId: UUID, rewardMetadataId: Long): UUID {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            RewardMetadataDao
                .update({ RewardMetadataDao.id eq rewardMetadataId }) {
                    it[RewardMetadataDao.availableAmount] = RewardMetadataDao.availableAmount - 1
                }

            CouponDao.insertAndGetId(code, userId, rewardMetadataId)
        }
    }
}