package com.ktor_sample.modules.user.repository.dao

import com.ktor_sample.foundation.database.customtable.TsUUIDTable

object UserDao : TsUUIDTable("user.user")