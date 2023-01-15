package com.ktor_sample.modules.points.repository.dao.transaction.type

import com.ktor_sample.modules.points.repository.dao.transaction.PointsTransactionDao
import com.ktor_sample.modules.points.repository.dao.transaction.type.base.PointsTransactionTable
import com.ktor_sample.modules.store.repository.dao.reward.RewardDao
import org.jetbrains.exposed.sql.insert
import java.util.*

object StoreRewardTransactionDao : PointsTransactionTable("point.store_reward_transaction") {
    val pointsTransactionId = uuid("points_transaction_id").references(PointsTransactionDao.id)
    val storeRewardId = uuid("store_reward_id").references(RewardDao.id)

    fun insertAndGetId(
        pointBalanceId: Long,
        storeRewardId: UUID,
        amount: Int,
        id: UUID? = null
    ): UUID {
        val pointTransactionId = insertAndGetId(
            pointBalanceId,
            amount,
            PointsTransactionDao.Type.STORE_REWARD_PURCHASE,
            id
        )

        insert {
            it[StoreRewardTransactionDao.pointsTransactionId] = pointTransactionId
            it[StoreRewardTransactionDao.storeRewardId] = storeRewardId
        }

        return pointTransactionId
    }
}
