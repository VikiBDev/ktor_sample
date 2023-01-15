package com.ktor_sample.foundation.controller.requestcontext

import com.ktor_sample.foundation.Language
import io.ktor.server.application.*

class RequestContext<T>(
    val call: ApplicationCall,
    val environment: KtorSampleEnvironment,
    val language: Language,
    val body: T
)