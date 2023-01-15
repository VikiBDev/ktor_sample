package com.ktor_sample.modules.store.repository.dao

import com.ktor_sample.foundation.database.customtable.TsUUIDTable
import com.ktor_sample.foundation.database.type.localisedText

object PromotionDao : TsUUIDTable("store.promotion") {
    val categoryId = integer("category_id").references(CategoryDao.id)
    val partnerId = integer("partner_id").references(PartnerDao.id)
    val localisedName = localisedText("localised_name")
    val localisedDescription = localisedText("localised_description")
    val imageUrl = varchar("image_url", 4096)
    val backgroundColor = char("background_color", 7)
    val textColor = varchar("text_color", 7)
}
