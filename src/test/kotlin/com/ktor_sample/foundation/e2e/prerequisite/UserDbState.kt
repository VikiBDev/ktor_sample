package com.ktor_sample.foundation.e2e.prerequisite

import com.ktor_sample.modules.auth.repository.dao.SessionDao
import com.ktor_sample.modules.auth.repository.dao.UserCredentialsDao
import com.ktor_sample.modules.user.repository.dao.UserDao
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert

class UserDbState(private val user: E2EUser) {
    fun applyState() {
        val newUserId = UserDao.insert { }[UserDao.id].value
        UserCredentialsDao.insert {
            it[userId] = newUserId
            it[username] = user.username
            it[hashedPassword] = user.password
        }
    }

    fun tearDown() {
        SessionDao.deleteWhere { userId eq UserDbUtil.getUserId(user)!! }
        UserDao.deleteWhere { id eq UserDbUtil.getUserId(user)!! }
    }
}