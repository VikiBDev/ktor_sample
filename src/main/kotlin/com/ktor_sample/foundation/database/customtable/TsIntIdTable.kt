package com.ktor_sample.foundation.database.customtable

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

open class TsIntIdTable(name: String = "", columnName: String = "id") : IntIdTable(name, columnName) {
    val createdAt = timestamp("created_at").default(Instant.now())
    val updatedAt = timestamp("updated_at").default(Instant.now())
}