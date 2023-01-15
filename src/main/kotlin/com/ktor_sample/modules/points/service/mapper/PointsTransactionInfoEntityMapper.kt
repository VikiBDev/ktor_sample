package com.ktor_sample.modules.points.service.mapper

import com.ktor_sample.modules.points.repository.model.PointsTransactionInfoEntity
import com.ktor_sample.modules.points.service.model.PointsTransactionInfo

object PointsTransactionInfoEntityMapper {
    fun convert(entity: PointsTransactionInfoEntity): PointsTransactionInfo =
        entity.run {
            PointsTransactionInfo(
                id,
                amount,
                createdAt
            )
        }
}