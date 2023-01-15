package com.ktor_sample.foundation.ktor

import com.ktor_sample.foundation.controller.requestcontext.KtorSampleEnvironment
import io.ktor.server.application.*
import org.koin.dsl.module

object KtorEnvKoinModule {
    fun instance(environment: ApplicationEnvironment) = module {
        single { KtorSampleEnvironment(environment) }
    }
}