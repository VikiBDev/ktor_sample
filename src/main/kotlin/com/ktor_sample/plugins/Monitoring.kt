package com.ktor_sample.plugins

import com.ktor_sample.API_V1_PREFIX
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith(API_V1_PREFIX) }
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]?.take(32)
            val userId = call.sessions.get<UserSession>()?.userId?.let { ", $it" } ?: ""

            "$status: $httpMethod ${call.request.path()} - userAgent: $userAgent - userId: $userId"
        }
    }
}

