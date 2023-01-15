package com.ktor_sample.modules.store.service.model

import com.ktor_sample.foundation.LocalisedString

data class Partner(
    val name: String,
    val websiteUrl: String,
    val logoUrl: String,
    val localisedDescription: LocalisedString
)