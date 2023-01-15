package com.ktor_sample.foundation.controller.requestcontext

import io.ktor.server.application.*
import org.slf4j.Logger

class KtorSampleEnvironment private constructor(val logger: Logger, val isDevelopmentMode: Boolean) {
    constructor(ktorEnvironment: ApplicationEnvironment) : this(ktorEnvironment.log, ktorEnvironment.developmentMode)
}