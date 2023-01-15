package com.ktor_sample.foundation.database.customtable

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

open class TsUUIDTable(name: String = "", columnName: String = "id") : UUIDTable(name, columnName) {
    val createdAt = timestamp("created_at").default(Instant.now())
    val updatedAt = timestamp("updated_at").default(Instant.now())
}