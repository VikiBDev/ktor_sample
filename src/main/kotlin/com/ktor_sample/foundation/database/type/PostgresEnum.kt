package com.ktor_sample.foundation.database.type

import org.postgresql.util.PGobject

/**
 * Mapper used by Exposed to translate Kotlin Enums into Postgres Enums and viceversa
 */
class PostgresEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}