package com.ktor_sample.modules.points.repository.dao

import com.ktor_sample.foundation.database.customtable.TsLongIdTable
import com.ktor_sample.modules.user.repository.dao.UserDao
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object PointBalanceDao : TsLongIdTable("point.points_balance") {
    val userId = uuid("user_id").references(UserDao.id)
    val amount = integer("amount")
    val lastSeenBalance = integer("last_seen_balance")
    val changedAt = timestamp("changed_at").default(Instant.now())
    val seenAt = timestamp("seen_at").default(Instant.now())
}
