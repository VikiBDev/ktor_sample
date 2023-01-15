package com.ktor_sample.foundation.session

import java.util.*

/**
 * Lives in foundation because every module will depend on this once we start splitting them up.
 */
interface SessionApi {
    suspend fun getSessionId(sessionId: UUID): UUID
}