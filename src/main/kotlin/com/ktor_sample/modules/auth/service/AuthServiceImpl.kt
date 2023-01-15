package com.ktor_sample.modules.auth.service

import com.ktor_sample.modules.auth.repository.AuthRepository
import com.ktor_sample.modules.auth.service.models.LoginResult
import java.time.Duration
import java.util.*

class AuthServiceImpl(private val authRepository: AuthRepository) : AuthService {
    override suspend fun loginWithPassword(username: String, password: String): LoginResult {
        val userId = authRepository.getUserIdFromCredentials(username, password)
        val sessionId = authRepository.createSession(userId, Duration.ofHours(6))
        return LoginResult(userId, sessionId)
    }

    override suspend fun logout(sessionId: UUID) {
        authRepository.expireSession(sessionId)
    }
}