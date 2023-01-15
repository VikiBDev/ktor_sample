package com.ktor_sample.modules.points.service.mapper

import com.ktor_sample.modules.points.repository.model.PointBalanceEntity
import com.ktor_sample.modules.points.service.model.PointBalance

object PointBalanceEntityMapper {
    fun convert(entity: PointBalanceEntity?): PointBalance =
        entity.run {
            PointBalance(this?.amount ?: 0, this?.lastSeenAmount ?: 0, this?.seen ?: true)
        }
}