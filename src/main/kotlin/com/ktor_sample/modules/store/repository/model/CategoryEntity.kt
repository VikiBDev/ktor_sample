package com.ktor_sample.modules.store.repository.model

import com.ktor_sample.foundation.LocalisedString

data class CategoryEntity(
    val id: Int,
    val name: String,
    val localisedName: LocalisedString
)