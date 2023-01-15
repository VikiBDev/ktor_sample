package com.ktor_sample.modules.store.repository.dao

import com.ktor_sample.foundation.database.customtable.TsTable

/**
 * Link table implementing a many-to-many relationship between a tag and a promotion
 */
object PromotionTagDao : TsTable("store.promotion_tag") {
    val promotionId = uuid("promotion_id").references(PromotionDao.id)
    val tagId = integer("tag_id").references(TagDao.id)
}
