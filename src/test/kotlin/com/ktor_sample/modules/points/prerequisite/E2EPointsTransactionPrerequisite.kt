package com.ktor_sample.modules.points.prerequisite

import com.ktor_sample.foundation.e2e.prerequisite.E2ETestPrerequisites
import com.ktor_sample.foundation.e2e.prerequisite.E2EUser
import com.ktor_sample.foundation.e2e.prerequisite.UserDbUtil
import com.ktor_sample.modules.points.repository.dao.PointBalanceDao
import com.ktor_sample.modules.points.repository.dao.transaction.PointsTransactionDao
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId

class E2EPointsTransactionPrerequisite : E2ETestPrerequisites() {
    override fun applyDbState(user: E2EUser?) {
        val balanceId = PointBalanceDao.insertAndGetId {
            it[userId] = UserDbUtil.getUserId(user!!) ?: return@insertAndGetId
            it[lastSeenBalance] = 500
            it[amount] = 500
        }.value

        PointsTransactionDao.insert {
            it[pointBalanceId] = balanceId
            it[amount] = 500
            it[type] = PointsTransactionDao.Type.INVOICE_PAYMENT
        }
    }

    override fun tearDownDbState(user: E2EUser?) {
        PointBalanceDao.deleteWhere { PointBalanceDao.userId eq UserDbUtil.getUserId(user!!)!! }
    }
}