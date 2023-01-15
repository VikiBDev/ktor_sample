package com.ktor_sample.modules.auth.repository

import java.time.Duration
import java.util.*

interface AuthRepository {
    suspend fun createSession(userId: UUID, duration: Duration): UUID
    suspend fun createUserCredentials(userId: UUID, username: String, hashedPassword: String): Long
    suspend fun expireSession(sessionId: UUID)
    suspend fun getUserIdFromCredentials(username: String, passwordHash: String): UUID
}