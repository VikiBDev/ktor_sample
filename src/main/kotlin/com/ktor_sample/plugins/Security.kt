package com.ktor_sample.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.util.*

fun Application.configureSecurity() {
    val secretEncryptKey = hex(System.getenv("USER_SESSION_ENCRYPT_KEY"))
    val secretSignKey = hex(System.getenv("USER_SESSION_SIGN_KEY"))
    install(Sessions) {
        header<UserSession>("user_session") {
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }
}

data class UserSession(val userId: UUID, val sessionId: UUID) : Principal