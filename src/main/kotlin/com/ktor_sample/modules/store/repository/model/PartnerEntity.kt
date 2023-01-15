package com.ktor_sample.modules.store.repository.model

import com.ktor_sample.foundation.LocalisedString

data class PartnerEntity(
    val id: Int,
    val name: String,
    val websiteUrl: String,
    val logoUrl: String,
    val localiseDescription: LocalisedString
)