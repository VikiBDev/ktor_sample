package com.ktor_sample.modules.points.repository.model

import java.util.*

data class PointBalanceEntity(
    val userId: UUID,
    val amount: Int,
    val lastSeenAmount: Int,
    val seen: Boolean
)
