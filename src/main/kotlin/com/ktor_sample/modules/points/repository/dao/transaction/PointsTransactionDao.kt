package com.ktor_sample.modules.points.repository.dao.transaction

import com.ktor_sample.foundation.database.customtable.TsUUIDTable
import com.ktor_sample.foundation.database.type.PostgresEnum
import com.ktor_sample.modules.points.repository.dao.PointBalanceDao

object PointsTransactionDao : TsUUIDTable("point.points_transaction") {
    val pointBalanceId = long("points_balance_id").references(PointBalanceDao.id)
    val amount = integer("amount")
    val type = PointsTransactionDao.customEnumeration(
        "type",
        "points_transaction_type",
        { value -> Type.valueOf(value as String) },
        { PostgresEnum("points_transaction_type", it) })

    enum class Type {
        STORE_REWARD_PURCHASE, INVOICE_PAYMENT
    }
}
