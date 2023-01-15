package com.ktor_sample.modules.store.repository.model

import com.ktor_sample.foundation.LocalisedString
import com.ktor_sample.modules.store.repository.dao.TagDao

data class TagEntity(
    val id: Int,
    val name: String,
    val localisedName: LocalisedString,
    val presentationMode: TagDao.PresentationMode
)