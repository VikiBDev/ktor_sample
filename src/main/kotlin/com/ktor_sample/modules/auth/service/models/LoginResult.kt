package com.ktor_sample.modules.auth.service.models

import java.util.UUID

data class LoginResult (val userId: UUID, val sessionId: UUID)