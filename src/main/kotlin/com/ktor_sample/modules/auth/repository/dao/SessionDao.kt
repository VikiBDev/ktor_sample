package com.ktor_sample.modules.auth.repository.dao

import com.ktor_sample.foundation.database.customtable.TsUUIDTable
import com.ktor_sample.modules.user.repository.dao.UserDao
import org.jetbrains.exposed.sql.javatime.timestamp

object SessionDao : TsUUIDTable("auth.session") {
    val userId = uuid("user_id").references(UserDao.id)
    val expiresAt = timestamp("expires_at")
}