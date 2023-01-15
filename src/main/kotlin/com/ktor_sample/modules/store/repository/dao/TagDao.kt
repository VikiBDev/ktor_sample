package com.ktor_sample.modules.store.repository.dao

import com.ktor_sample.foundation.database.customtable.TsIntIdTable
import com.ktor_sample.foundation.database.type.PostgresEnum
import com.ktor_sample.foundation.database.type.localisedText

object TagDao : TsIntIdTable("store.tag") {
    val name = varchar("name", 128)
    val localisedName = localisedText("localised_name")
    val presentationMode = customEnumeration(
        "presentation_mode",
        "PromotionPresentationMode",
        { value -> PresentationMode.valueOf(value as String) },
        { PostgresEnum("PromotionPresentationMode", it) }).default(PresentationMode.DEFAULT)

    enum class PresentationMode {
        DEFAULT, LARGE
    }
}
