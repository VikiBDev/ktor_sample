package com.ktor_sample.foundation.database

import com.ktor_sample.plugins.FlywayMigrator
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import javax.sql.DataSource

object DatabaseKoinModule {
    fun instance(dataSource: DataSource) = module {
        single {
            Database.connect(dataSource)
        }

        factory<Flyway> { params ->
            Flyway.configure()
                .locations("db/migration/${params.get<String>()}")
                .schemas(params.get())
                .dataSource(dataSource)
                .validateMigrationNaming(true)
                .load()
        }

        single {
            FlywayMigrator(
                get { parametersOf("core") },
                get { parametersOf("user") },
                get { parametersOf("auth") },
                get { parametersOf("store") },
                get { parametersOf("point") }
            )
        }
    }
}