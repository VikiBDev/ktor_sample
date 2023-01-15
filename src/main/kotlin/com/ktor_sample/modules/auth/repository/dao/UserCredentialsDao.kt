package com.ktor_sample.modules.auth.repository.dao

import com.ktor_sample.foundation.database.customtable.TsLongIdTable
import com.ktor_sample.modules.user.repository.dao.UserDao

object UserCredentialsDao : TsLongIdTable("auth.user_credentials") {
    val userId = uuid("user_id").references(UserDao.id)
    val username = varchar("username", 64)
    val hashedPassword = varchar("password_hashed", 128)
}