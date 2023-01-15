package com.ktor_sample.foundation.database.function

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Function
import org.jetbrains.exposed.sql.IColumnType
import org.jetbrains.exposed.sql.QueryBuilder

class SqlLocate<T>(_columnType: IColumnType, private val expr: Expression<*>, private val reference: String) :
    Function<T>(_columnType) {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) = queryBuilder {
        append("POSITION(LOWER(")
        append(expr)
        append(") in LOWER('$reference')")
        append(")")
    }
}
