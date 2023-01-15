package com.ktor_sample.modules.auth.service

import com.ktor_sample.modules.auth.service.models.LoginResult
import java.util.*

interface AuthService {
    suspend fun loginWithPassword(username: String, password: String): LoginResult
    suspend fun logout(sessionId: UUID)
}