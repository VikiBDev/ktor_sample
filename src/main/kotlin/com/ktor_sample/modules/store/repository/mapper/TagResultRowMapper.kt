package com.ktor_sample.modules.store.repository.mapper

import com.ktor_sample.modules.store.repository.dao.TagDao
import com.ktor_sample.modules.store.repository.model.TagEntity
import org.jetbrains.exposed.sql.ResultRow

object TagResultRowMapper {
    fun convert(row: ResultRow): TagEntity =
        row.run {
            TagEntity(
                this[TagDao.id].value,
                this[TagDao.name],
                this[TagDao.localisedName],
                this[TagDao.presentationMode]
            )
        }
}
