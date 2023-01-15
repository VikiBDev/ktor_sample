package com.ktor_sample.modules.points

import com.ktor_sample.modules.points.controller.ChangePointsController
import com.ktor_sample.modules.points.controller.GetPointsBalanceController
import com.ktor_sample.modules.points.controller.GetPointsTransactionController
import com.ktor_sample.modules.points.controller.GetPointsTransactionsController
import com.ktor_sample.modules.points.controller.model.PointsTransactionWrapperView
import com.ktor_sample.modules.points.service.model.PointBalance
import com.ktor_sample.modules.points.service.model.PointsTransactionInfo
import com.ktor_sample.foundation.kompendium.NotarizedRouteRegexFixed
import com.ktor_sample.plugins.AUTH_SESSION_NAME
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

internal data class PointAmount(val amount: Int)

internal fun Route.pointsRouter(
    getPointsBalanceController: GetPointsBalanceController,
    getPointsTransactionsController: GetPointsTransactionsController,
    changePointsController: ChangePointsController,
    getPointsTransactionController: GetPointsTransactionController
) {
    route("/points") {
        authenticate(AUTH_SESSION_NAME) {
            get("/balance") {
                getPointsBalanceController.execute(call)
            }.getBalanceDoc()
            get("/transactions/{transactionId}") {
                getPointsTransactionController.execute(call)
            }.getTransactionDoc()
            get("/transactions") {
                getPointsTransactionsController.execute(call)
            }.getTransactionsDoc()

            //TODO remove after testing (or decide whether to keep it in dev only mode)
            post<PointAmount>("/change-by") {
                changePointsController.execute(call, it)
            }
        }
    }
}

// region docs
private fun Route.getBalanceDoc() {
    install(NotarizedRouteRegexFixed()) {
        tags = setOf("points")

        get = GetInfo.builder {
            summary("Get points balance")
            description("Returns the points balance for the current user")
            response {
                responseCode(HttpStatusCode.OK)
                responseType<PointBalance>()
                description("The points balance, including info about the amounts last seen from the client")
            }
        }
    }
}

private fun Route.getTransactionDoc() {
    install(NotarizedRouteRegexFixed()) {
        tags = setOf("points")

        get = GetInfo.builder {
            summary("Get specific points transaction")
            description(
                """
                Get a points transaction, which can either be a debit (if it's related to a Store reward purchase"),
                or a credit (if related to an invoice repayment)
                 """.trimIndent()
            )
            parameters(
                Parameter(
                    "transactionId",
                    Parameter.Location.path,
                    TypeDefinition.UUID,
                    description = "The UUID of the points transaction"
                )
            )
            response {
                responseCode(HttpStatusCode.OK)
                responseType<PointsTransactionWrapperView>()
                description("The requested points transaction.")
            }
        }
    }
}

private fun Route.getTransactionsDoc() {
    install(NotarizedRouteRegexFixed()) {
        tags = setOf("points")

        get = GetInfo.builder {
            summary("Get all points transactions")
            description(
                """
                Get all the points transactions for the current user. Contains only a small amount of info
                for each transaction, since the bulk of each transaction details can be retrieved via the
                /transactions/{transactionId} endpoint.
                 """.trimIndent()
            )
            response {
                responseCode(HttpStatusCode.OK)
                responseType<List<PointsTransactionInfo>>()
                description("List of transactions performed by the current user")
            }
        }
    }
}
// endregion
