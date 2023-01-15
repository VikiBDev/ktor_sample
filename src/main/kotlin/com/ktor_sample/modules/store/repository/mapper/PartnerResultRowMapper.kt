package com.ktor_sample.modules.store.repository.mapper

import com.ktor_sample.modules.store.repository.dao.PartnerDao
import com.ktor_sample.modules.store.repository.model.PartnerEntity
import org.jetbrains.exposed.sql.ResultRow

object PartnerResultRowMapper {
    fun convert(row: ResultRow): PartnerEntity =
        row.run {
            PartnerEntity(
                this[PartnerDao.id].value,
                this[PartnerDao.name],
                this[PartnerDao.websiteUrl],
                this[PartnerDao.logoUrl],
                this[PartnerDao.localiseDescription]
            )
        }
}