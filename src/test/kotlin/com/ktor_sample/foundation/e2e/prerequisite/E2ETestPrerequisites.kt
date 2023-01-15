package com.ktor_sample.foundation.e2e.prerequisite

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

abstract class E2ETestPrerequisites(
    val user: E2EUser? = E2EUser(UUID.randomUUID().toString(), "e2etest"),
) {
    fun setup(database: Database) {
        transaction(database) {
            user?.let { UserDbState(user).applyState() }
            applyDbState(user)
        }
    }

    fun tearDown(database: Database) {
        transaction(database) {
            tearDownDbState(user)
            user?.let { UserDbState(user).tearDown() }
        }
    }

    abstract fun applyDbState(user: E2EUser?)
    abstract fun tearDownDbState(user: E2EUser?)

    class LoggedOut : E2ETestPrerequisites(user = null) {
        override fun applyDbState(user: E2EUser?) {}

        override fun tearDownDbState(user: E2EUser?) {}
    }

    class Default : E2ETestPrerequisites() {
        override fun applyDbState(user: E2EUser?) {}

        override fun tearDownDbState(user: E2EUser?) {}
    }
}
