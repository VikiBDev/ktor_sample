package com.ktor_sample.foundation.http

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*

/**
 * Building that abstracts some boilerplate in the instantiation of an [HttpClient]
 */
class HttpClientBuilder {
    var logLevel: LogLevel = LogLevel.INFO
    var namingStrategy: PropertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
    var throwExceptionOnFail = true

    companion object {
        operator fun invoke(block: HttpClientBuilder.() -> Unit): HttpClient {
            val builder = HttpClientBuilder().apply(block)

            return HttpClient(CIO) {
                install(Logging) {
                    level = builder.logLevel
                }
                install(ContentNegotiation) {
                    jackson(ContentType.Application.FormUrlEncoded) {
                        propertyNamingStrategy = builder.namingStrategy
                        registerModules(JavaTimeModule())
                    }

                    jackson(ContentType.Application.Json) {
                        propertyNamingStrategy = builder.namingStrategy
                        registerModules(JavaTimeModule())
                    }
                }

                expectSuccess = builder.throwExceptionOnFail
            }
        }
    }
}