package com.ktor_sample.modules.points.service.model

import java.time.Instant
import java.util.*

data class PointsTransactionInfo(
    val id: UUID,
    val amount: Int,
    val createdAt: Instant
)
