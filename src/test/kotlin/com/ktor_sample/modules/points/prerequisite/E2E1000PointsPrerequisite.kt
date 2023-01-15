package com.ktor_sample.modules.points.prerequisite

import com.ktor_sample.foundation.e2e.prerequisite.E2ETestPrerequisites
import com.ktor_sample.foundation.e2e.prerequisite.E2EUser
import com.ktor_sample.foundation.e2e.prerequisite.UserDbUtil
import com.ktor_sample.modules.points.repository.dao.PointBalanceDao
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert

class E2E1000PointsPrerequisite : E2ETestPrerequisites() {
    override fun applyDbState(user: E2EUser?) {
        PointBalanceDao.insert {
            it[userId] = UserDbUtil.getUserId(user!!) ?: return@insert
            it[lastSeenBalance] = 1000
            it[amount] = 1000
        }
    }

    override fun tearDownDbState(user: E2EUser?) {
        PointBalanceDao.deleteWhere { PointBalanceDao.userId eq UserDbUtil.getUserId(user!!)!! }
    }
}