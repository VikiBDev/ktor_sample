package com.ktor_sample.foundation.controller.exception.core

import java.util.*

abstract class BaseException(val errorCode: String, val originalException: Exception?) : Exception() {
    val traceId: UUID = UUID.randomUUID()
}