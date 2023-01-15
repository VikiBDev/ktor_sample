package com.ktor_sample.foundation.http

/**
 * Class used for endpoints that return an empty response. Implements equal in order to enable easier testing
 */
object Empty {
    override fun equals(other: Any?): Boolean {
        return true
    }

    override fun toString(): String {
        return "Empty"
    }

    override fun hashCode(): Int {
        return 0
    }
}