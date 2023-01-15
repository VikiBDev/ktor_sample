package com.ktor_sample.modules.points.service.model

data class PointBalance(
    val amount: Int,
    val lastSeenAmount: Int,
    val seen: Boolean
)
