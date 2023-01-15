package com.ktor_sample.plugins

import com.ktor_sample.foundation.session.SessionApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.koin.java.KoinJavaComponent.getKoin

const val AUTH_SESSION_NAME = "user-auth-session"
fun Application.configureAuthentication() {
    install(Authentication) {
        session<UserSession>(AUTH_SESSION_NAME) {
            val sessionApi: SessionApi = getKoin().get()
            validate { session ->
                try {
                    sessionApi.getSessionId(session.sessionId)
                    session
                } catch (e: Exception) {
                    null
                }
            }

            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        basic("docs") {
            validate {
                if (it.name == System.getenv("OAS_USER") && it.password == System.getenv("OAS_PSW"))
                    UserIdPrincipal("docs")
                else
                    null
            }
        }
    }
}
