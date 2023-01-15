package com.ktor_sample.foundation.controller.exception.core

open class SilentException(override val message: String, errorCode: String, originalException: Exception? = null) :
    BaseException(errorCode, originalException)
