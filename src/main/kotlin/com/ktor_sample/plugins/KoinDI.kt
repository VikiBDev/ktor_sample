package com.ktor_sample.plugins

import com.ktor_sample.foundation.database.DatabaseKoinModule
import com.ktor_sample.foundation.http.HttpKoinModule
import com.ktor_sample.foundation.ktor.KtorEnvKoinModule
import com.ktor_sample.foundation.session.SessionKoinModule
import com.ktor_sample.modules.auth.di.AuthKoinModule
import com.ktor_sample.modules.points.di.PointsKoinModule
import com.ktor_sample.modules.store.di.StoreKoinModule
import io.ktor.client.*
import io.ktor.server.application.*
import org.koin.core.context.GlobalContext
import javax.sql.DataSource

fun Application.configureDI(camelCaseHttpClient: HttpClient, snakeCaseHttpClient: HttpClient, dataSource: DataSource) {
    GlobalContext.startKoin {
        modules(
            HttpKoinModule.instance(camelCaseHttpClient, snakeCaseHttpClient),
            KtorEnvKoinModule.instance(environment),
            DatabaseKoinModule.instance(dataSource),
            SessionKoinModule.instance,
            AuthKoinModule.instance,
            StoreKoinModule.instance,
            PointsKoinModule.instance
        )
    }
}