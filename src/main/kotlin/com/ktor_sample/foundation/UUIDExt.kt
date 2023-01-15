package com.ktor_sample.foundation

import java.util.*

object UUIDUtils {
    fun fromRawString(name: String): UUID {
        if (name.length != 32) throw InvalidUUIDException("Invalid UUID $name string provided")

        val input =
            "${name.substring(0..7)}-${name.substring(8..11)}-${name.substring(12..15)}-${name.substring(16..19)}-${
                name.substring(20..31)
            }"
        return UUID.fromString(input)
    }
}

fun UUID.toRawString() = toString().replace("-", "")

class InvalidUUIDException(message: String) : Exception(message)