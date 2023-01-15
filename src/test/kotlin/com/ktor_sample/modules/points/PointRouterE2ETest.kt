package com.ktor_sample.modules.points

import com.ktor_sample.foundation.Result
import com.ktor_sample.foundation.e2e.KtorE2ETest
import com.ktor_sample.modules.points.exception.NotEnoughPointsException
import com.ktor_sample.modules.points.prerequisite.E2ENoPointsPrerequisite
import com.ktor_sample.modules.points.prerequisite.E2E1000PointsPrerequisite
import com.ktor_sample.modules.points.prerequisite.E2EPointsTransactionPrerequisite
import com.ktor_sample.modules.points.service.model.PointBalance
import com.ktor_sample.modules.points.service.model.PointsTransactionInfo
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class PointRouterE2ETest : KtorE2ETest() {

    private val noPointsPrerequisite = E2ENoPointsPrerequisite()
    private val pointsPrerequisite = E2E1000PointsPrerequisite()
    private val pointsTransactionPrerequisite = E2EPointsTransactionPrerequisite()

    @Test
    fun `Get points balance with empty user`() = testApp(noPointsPrerequisite) {
        val response = camelCaseHttpClient.get("/api/v1/points/balance")

        val responseBody: Result.Success<PointBalance> = response.body()

        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(
            Result.Success(PointBalance(0, 0, true)),
            responseBody
        )
    }

    @Test
    fun `Get points balance with user that has points`() = testApp(pointsPrerequisite) {
        val response = camelCaseHttpClient.get("/api/v1/points/balance")

        val responseBody: Result.Success<PointBalance> = response.body()

        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(
            Result.Success(PointBalance(1000, 1000, true)),
            responseBody
        )
    }

    @Test
    fun `Add points on user with no balance`() = testApp(noPointsPrerequisite) {
        val addPointsResponse = camelCaseHttpClient.post("/api/v1/points/change-by") {
            contentType(ContentType.Application.Json)
            setBody(PointAmount(500))
        }

        Assertions.assertEquals(HttpStatusCode.OK, addPointsResponse.status)

        val response = camelCaseHttpClient.get("/api/v1/points/balance")
        val responseBody: Result.Success<PointBalance> = response.body()

        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(
            Result.Success(PointBalance(500, 0, false)),
            responseBody
        )
    }

    @Test
    fun `Add points on user with existing balance`() = testApp(pointsPrerequisite) {
        val addPointsResponse = camelCaseHttpClient.post("/api/v1/points/change-by") {
            contentType(ContentType.Application.Json)
            setBody(PointAmount(500))
        }

        Assertions.assertEquals(HttpStatusCode.OK, addPointsResponse.status)

        val response = camelCaseHttpClient.get("/api/v1/points/balance")
        val responseBody: Result.Success<PointBalance> = response.body()

        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(
            Result.Success(PointBalance(1500, 1000, false)),
            responseBody
        )
    }

    @Test
    fun `Remove points on user with existing balance`() = testApp(pointsPrerequisite) {
        val addPointsResponse = camelCaseHttpClient.post("/api/v1/points/change-by") {
            contentType(ContentType.Application.Json)
            setBody(PointAmount(-500))
        }

        Assertions.assertEquals(HttpStatusCode.OK, addPointsResponse.status)

        val response = camelCaseHttpClient.get("/api/v1/points/balance")
        val responseBody: Result.Success<PointBalance> = response.body()

        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(
            Result.Success(PointBalance(500, 1000, false)),
            responseBody
        )
    }

    @Test
    fun `Remove points on user with no balance`() = testApp(noPointsPrerequisite) {
        val response = camelCaseHttpClient.post("/api/v1/points/change-by") {
            contentType(ContentType.Application.Json)
            setBody(PointAmount(-500))
        }

        val responseBody: Result.Error = response.body()

        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(NotEnoughPointsException.errorCode, responseBody.errorCode)
    }

    @Test
    fun `Remove points on user with too little balance`() = testApp(pointsPrerequisite) {
        val response = camelCaseHttpClient.post("/api/v1/points/change-by") {
            contentType(ContentType.Application.Json)
            setBody(PointAmount(-2000))
        }

        val responseBody: Result.Error = response.body()

        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(NotEnoughPointsException.errorCode, responseBody.errorCode)
    }

    @Test
    fun `Add points on user with existing balance multiple times`() = testApp(pointsPrerequisite) {
        val addPointsResponse = camelCaseHttpClient.post("/api/v1/points/change-by") {
            contentType(ContentType.Application.Json)
            setBody(PointAmount(500))
        }

        Assertions.assertEquals(HttpStatusCode.OK, addPointsResponse.status)

        val anotherAddPointsResponse = camelCaseHttpClient.post("/api/v1/points/change-by") {
            contentType(ContentType.Application.Json)
            setBody(PointAmount(1200))
        }

        Assertions.assertEquals(HttpStatusCode.OK, anotherAddPointsResponse.status)

        val response = camelCaseHttpClient.get("/api/v1/points/balance")
        val responseBody: Result.Success<PointBalance> = response.body()

        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(
            Result.Success(PointBalance(2700, 1000, false)),
            responseBody
        )
    }

    @Test
    fun `Add points on user without existing balance multiple times`() = testApp(noPointsPrerequisite) {
        val addPointsResponse = camelCaseHttpClient.post("/api/v1/points/change-by") {
            contentType(ContentType.Application.Json)
            setBody(PointAmount(500))
        }

        Assertions.assertEquals(HttpStatusCode.OK, addPointsResponse.status)

        val anotherAddPointsResponse = camelCaseHttpClient.post("/api/v1/points/change-by") {
            contentType(ContentType.Application.Json)
            setBody(PointAmount(1200))
        }

        Assertions.assertEquals(HttpStatusCode.OK, anotherAddPointsResponse.status)

        val response = camelCaseHttpClient.get("/api/v1/points/balance")
        val responseBody: Result.Success<PointBalance> = response.body()

        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(
            Result.Success(PointBalance(1700, 0, false)),
            responseBody
        )
    }

    @Test
    fun `Get all points transaction`() = testApp(pointsTransactionPrerequisite) {
        val response = camelCaseHttpClient.get("/api/v1/points/transactions")
        val responseBody: Result.Success<List<PointsTransactionInfo>> = response.body()

        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(1, responseBody.value.size)
        Assertions.assertEquals(500, responseBody.value.first().amount)
    }

}
