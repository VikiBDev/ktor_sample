package com.ktor_sample.foundation.e2e.prerequisite

import com.ktor_sample.modules.auth.repository.dao.UserCredentialsDao
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import java.util.*

object UserDbUtil {
    fun getUserId(user: E2EUser): UUID? {
        return UserCredentialsDao
            .slice(UserCredentialsDao.userId)
            .select(UserCredentialsDao.username.eq(user.username) and UserCredentialsDao.hashedPassword.eq(user.password))
            .singleOrNull()?.get(UserCredentialsDao.userId)
    }
}