package com.ktor_sample

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.ktor_sample.foundation.http.HttpClientBuilder
import com.ktor_sample.plugins.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.client.plugins.logging.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.getKoin

fun main() {
    val snakeCaseHttpClient = HttpClientBuilder {
        logLevel = LogLevel.ALL
        namingStrategy = PropertyNamingStrategies.SNAKE_CASE
    }

    val camelCaseHttpClient = HttpClientBuilder {
        logLevel = LogLevel.ALL
        namingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
    }

    val dataSource = run {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:${System.getenv("MAIN_DB_URL")}"
        config.username = System.getenv("MAIN_DB_USERNAME")
        config.password = System.getenv("MAIN_DB_PSW")
        config.maximumPoolSize = 10
        HikariDataSource(config)
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureDI(camelCaseHttpClient, snakeCaseHttpClient, dataSource)
        getKoin().get<FlywayMigrator>().migrate()
        configureSecurity()
        configureAuthentication()
        configureDocumentation()
        configureHTTP()
        configureMonitoring()
        configureSerialization()
        configureRouting()
        configureModules()
    }.start(wait = true)
}
