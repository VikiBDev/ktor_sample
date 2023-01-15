package com.ktor_sample.foundation.controller.exception.core

open class LoudException(
    val title: String,
    override val message: String,
    errorCode: String,
    originalException: Exception? = null
) : BaseException(errorCode, originalException)
