package com.ktor_sample.foundation.http

import io.ktor.client.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

object HttpKoinModule {
    fun instance(camelCaseHttpClient: HttpClient, snakeCaseHttpClient: HttpClient) = module {
        // Http clients
        single(named("camelCase")) { camelCaseHttpClient }
        single(named("snakeCase")) { snakeCaseHttpClient }
    }
}