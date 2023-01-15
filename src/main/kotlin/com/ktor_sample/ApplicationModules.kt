package com.ktor_sample

import com.ktor_sample.modules.auth.authRouter
import com.ktor_sample.modules.points.pointsRouter
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

const val API_V1_PREFIX = "/api/v1"

fun Application.configureModules() {
    routing {
        route(API_V1_PREFIX) {
            authRouter(get())
            pointsRouter(get(), get(), get(), get())
        }
    }
}