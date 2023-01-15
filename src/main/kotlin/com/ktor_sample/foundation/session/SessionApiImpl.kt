package com.ktor_sample.foundation.session

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

/**
 * Note for reviewer: This implementation always work as if a session was always available, just to keep things simple
 * and avoid having to import the whole session module in this test project
 */
class SessionApiImpl(private val database: Database) : SessionApi {
    override suspend fun getSessionId(sessionId: UUID): UUID {
        return newSuspendedTransaction(Dispatchers.IO, db = database) {
            return@newSuspendedTransaction UUID.randomUUID()
        }
    }
}