package com.ktor_sample.foundation.session

import org.koin.dsl.module

object SessionKoinModule {
    val instance = module {
        // Create a standalone connection when this gets move to a common library (once we start splitting microservices)
        single<SessionApi> { SessionApiImpl(get()) }
    }
}