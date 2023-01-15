package com.ktor_sample.modules.points.repository.mapper

import com.ktor_sample.modules.points.repository.dao.PointBalanceDao
import com.ktor_sample.modules.points.repository.model.PointBalanceEntity
import org.jetbrains.exposed.sql.ResultRow

object PointBalanceResultRowMapper {
    fun convert(row: ResultRow): PointBalanceEntity =
        row.run {
            PointBalanceEntity(
                this[PointBalanceDao.userId],
                this[PointBalanceDao.amount],
                this[PointBalanceDao.lastSeenBalance],
                this[PointBalanceDao.seenAt] >= this[PointBalanceDao.changedAt]
            )
        }
}
