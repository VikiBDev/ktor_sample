package com.ktor_sample.foundation.db

import com.ktor_sample.foundation.database.DatabaseKoinModule
import com.zaxxer.hikari.util.DriverDataSource
import org.koin.dsl.koinApplication
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.util.*
import javax.sql.DataSource

class DockerPostgresContainer(image: DockerImageName) : PostgreSQLContainer<DockerPostgresContainer>(image) {
    companion object {
        val dataSource: DataSource by lazy {
            DriverDataSource(
                instance.jdbcUrl,
                "org.postgresql.Driver",
                Properties(),
                instance.username,
                instance.password
            )
        }

        var migrated = false

        val koinApp by lazy {
            koinApplication {
                modules(DatabaseKoinModule.instance(dataSource))
            }
        }

        private val instance: DockerPostgresContainer by lazy {
            val docker = DockerPostgresContainer(DockerImageName.parse("postgres:14.6-alpine")).apply {
                withUsername("root")
                withPassword("password")
                withDatabaseName("e2eTest")
                withReuse(true)
                portBindings = listOf("999:5432")
            }
            docker.start()
            docker
        }
    }
}
