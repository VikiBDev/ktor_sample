package com.ktor_sample.modules.points.repository.model

import java.time.Instant
import java.util.*

/**
 * Simple class containing the basic info for a points transaction. For a more complete class, containing all
 * the info related to a points transaction, see [PointsTransactionEntity]
 */
data class PointsTransactionInfoEntity(
    val id: UUID,
    val amount: Int,
    val createdAt: Instant
)
