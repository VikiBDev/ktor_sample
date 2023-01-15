package com.ktor_sample.foundation

import com.ktor_sample.plugins.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun ApplicationCall.userId() = principal<UserSession>()!!.userId