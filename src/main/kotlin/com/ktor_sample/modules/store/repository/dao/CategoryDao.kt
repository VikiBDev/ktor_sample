package com.ktor_sample.modules.store.repository.dao

import com.ktor_sample.foundation.database.customtable.TsIntIdTable
import com.ktor_sample.foundation.database.type.localisedText

object CategoryDao : TsIntIdTable("store.category") {
    val name = varchar("name", 128)
    val localisedName = localisedText("localised_name")
}

