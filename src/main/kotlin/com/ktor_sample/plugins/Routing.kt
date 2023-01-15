package com.ktor_sample.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/_health") {
            call.respond(HttpStatusCode.OK, "I'm alive and well!")
        }
    }
}
