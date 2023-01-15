package com.ktor_sample.modules.auth

import com.ktor_sample.foundation.Result
import com.ktor_sample.foundation.http.Empty
import com.ktor_sample.modules.auth.service.AuthService
import com.ktor_sample.plugins.UserSession
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

data class UsernameAndPassword(val username: String, val password: String)

fun Route.authRouter(authService: AuthService) {
    route("/auth") {
        post<UsernameAndPassword>("/password-login") {
            val loginResult = authService.loginWithPassword(it.username, it.password)
            call.sessions.set(UserSession(loginResult.userId, loginResult.sessionId))
            call.respond(Result.Success(Empty))
        }
    }
}
