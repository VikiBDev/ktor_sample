package com.ktor_sample.foundation

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ktor_sample.foundation.Result.Error
import com.ktor_sample.foundation.Result.Success
import java.util.*

/**
 * Helper class defining a computation result. Can either be a [Success], containing the returned
 * value, or an [Error], containing a description of what went wrong
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = Success::class, name = "success"),
    JsonSubTypes.Type(value = Error::class, name = "error")
)
sealed class Result {
    data class Success<T>(val value: T) : Result()
    data class Error(
        val message: String,
        val traceId: UUID,
        val errorCode: String,
        val title: String? = null
    ) : Result()
}

fun <T> T.toResult(): Result = Success(this)