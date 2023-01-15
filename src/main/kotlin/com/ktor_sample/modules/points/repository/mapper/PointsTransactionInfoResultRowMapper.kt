package com.ktor_sample.modules.points.repository.mapper

import com.ktor_sample.modules.points.repository.dao.transaction.PointsTransactionDao
import com.ktor_sample.modules.points.repository.model.PointsTransactionInfoEntity
import org.jetbrains.exposed.sql.ResultRow

object PointsTransactionInfoResultRowMapper {
    fun convert(row: ResultRow): PointsTransactionInfoEntity =
        row.run {
            PointsTransactionInfoEntity(
                row[PointsTransactionDao.id].value,
                row[PointsTransactionDao.amount],
                row[PointsTransactionDao.createdAt]
            )
        }
}