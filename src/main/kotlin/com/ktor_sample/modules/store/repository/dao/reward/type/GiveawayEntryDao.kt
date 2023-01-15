package com.ktor_sample.modules.store.repository.dao.reward.type

import com.ktor_sample.modules.store.repository.dao.reward.RewardDao
import com.ktor_sample.modules.store.repository.dao.reward.type.base.RewardTable
import org.jetbrains.exposed.sql.insert
import java.util.*

object GiveawayEntryDao : RewardTable("store.giveaway_entry") {
    val rewardId = uuid("reward_id").references(RewardDao.id)

    fun insertAndGetId(userId: UUID, rewardMetadataId: Long): UUID {
        val rewardId = insertAndGetId(userId, rewardMetadataId)
        insert {
            it[GiveawayEntryDao.rewardId] = rewardId
        }

        return rewardId
    }
}
