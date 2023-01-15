package com.ktor_sample.modules.store.repository.dao.reward

import com.ktor_sample.foundation.database.customtable.TsUUIDTable
import com.ktor_sample.modules.store.repository.dao.rewardmetadata.RewardMetadataDao
import com.ktor_sample.modules.user.repository.dao.UserDao

object RewardDao : TsUUIDTable("store.reward") {
    val userId = uuid("user_id").references(UserDao.id)
    val rewardMetadataId = long("reward_metadata_id").references(RewardMetadataDao.id)
}
