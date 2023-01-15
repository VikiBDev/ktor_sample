package com.ktor_sample.modules.store.repository.dao

import com.ktor_sample.foundation.database.customtable.TsIntIdTable
import com.ktor_sample.foundation.database.type.localisedText

object PartnerDao : TsIntIdTable("store.partner") {
    val name = varchar("name", 128)
    val websiteUrl = varchar("website_url", 2048)
    val logoUrl = varchar("logo_url", 2048)
    val localiseDescription = localisedText("localised_description")
}


