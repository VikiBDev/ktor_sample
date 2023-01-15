package com.ktor_sample.modules.store.repository.mapper

import com.ktor_sample.modules.store.repository.dao.CategoryDao
import com.ktor_sample.modules.store.repository.dao.PromotionDao
import com.ktor_sample.modules.store.repository.model.PromotionEntity
import org.jetbrains.exposed.sql.ResultRow

object PromotionResultRowMapper {
    fun convert(row: ResultRow): PromotionEntity =
        row.run {
            PromotionEntity(
                this[PromotionDao.id].value,
                this[PromotionDao.categoryId],
                this[PromotionDao.partnerId],
                this[PromotionDao.localisedName],
                this[PromotionDao.localisedDescription],
                this[PromotionDao.imageUrl],
                this[PromotionDao.backgroundColor],
                this[PromotionDao.textColor],
                this[CategoryDao.name],
                this[CategoryDao.localisedName],
                PartnerResultRowMapper.convert(row),
                RewardMetadataResultRowMapper.convert(row)
            )
        }
}
