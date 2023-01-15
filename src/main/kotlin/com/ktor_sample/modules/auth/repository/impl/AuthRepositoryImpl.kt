package com.ktor_sample.modules.auth.repository.impl

import com.ktor_sample.modules.auth.repository.AuthRepository
import com.ktor_sample.modules.auth.repository.dao.SessionDao
import com.ktor_sample.modules.auth.repository.dao.UserCredentialsDao
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Duration
import java.time.Instant
import java.util.*

class AuthRepositoryImpl(private val database: Database) : AuthRepository {
    override suspend fun createSession(userId: UUID, duration: Duration): UUID {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            val sessionId = SessionDao.insert {
                it[this.userId] = userId
                it[this.expiresAt] = Instant.now().plus(Duration.ofHours(24))
            } get SessionDao.id

            sessionId.value
        }
    }

    override suspend fun expireSession(sessionId: UUID) {
        newSuspendedTransaction(Dispatchers.IO, database) {
            SessionDao.update({ SessionDao.id eq sessionId }) {
                it[expiresAt] = Instant.now()
            }
        }
    }

    override suspend fun createUserCredentials(userId: UUID, username: String, hashedPassword: String): Long {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            UserCredentialsDao.insert {
                it[this.userId] = userId
                it[this.username] = username
                it[this.hashedPassword] = hashedPassword
            }[UserCredentialsDao.id].value
        }
    }

    override suspend fun getUserIdFromCredentials(username: String, passwordHash: String): UUID {
        return newSuspendedTransaction(Dispatchers.IO, database) {
            UserCredentialsDao
                .select(UserCredentialsDao.username.eq(username) and UserCredentialsDao.hashedPassword.eq(passwordHash))
                .single()[UserCredentialsDao.userId]
        }
    }
}