package com.ktor_sample.foundation.controller.exception

import com.ktor_sample.foundation.controller.exception.core.SilentException

class MissingParameterException(paramName: String) :
    SilentException("Missing required parameter: $paramName", errorCode) {
    companion object {
        const val errorCode = "missing_required_param"
    }
}