package com.ktor_sample.foundation.e2e.prerequisite

open class E2EUser(val username: String, val password: String)

object DefaultUser: E2EUser("e2e_frodo", "onering")