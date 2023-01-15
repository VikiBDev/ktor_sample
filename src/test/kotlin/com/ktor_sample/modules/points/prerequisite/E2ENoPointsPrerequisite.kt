package com.ktor_sample.modules.points.prerequisite

import com.ktor_sample.foundation.e2e.prerequisite.E2ETestPrerequisites
import com.ktor_sample.foundation.e2e.prerequisite.E2EUser
import com.ktor_sample.foundation.e2e.prerequisite.UserDbUtil
import com.ktor_sample.modules.points.repository.dao.PointBalanceDao
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class E2ENoPointsPrerequisite : E2ETestPrerequisites() {
    override fun applyDbState(user: E2EUser?) {
        PointBalanceDao.deleteWhere { PointBalanceDao.userId eq UserDbUtil.getUserId(user!!)!! }
    }

    override fun tearDownDbState(user: E2EUser?) {}
}