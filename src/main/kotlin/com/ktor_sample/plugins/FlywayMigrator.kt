package com.ktor_sample.plugins

import org.flywaydb.core.Flyway


class FlywayMigrator constructor(private val schemas: List<Flyway>) {
    constructor(vararg schemas: Flyway) : this(schemas.toList())

    fun migrate() {
        schemas.forEach {
            it.migrate()
        }
    }

    fun drop() {
        schemas.forEach {
            it.clean()
        }
    }
}