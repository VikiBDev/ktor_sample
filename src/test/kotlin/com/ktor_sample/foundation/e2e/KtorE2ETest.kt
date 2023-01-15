package com.ktor_sample.foundation.e2e

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.ktor_sample.configureModules
import com.ktor_sample.foundation.Result
import com.ktor_sample.foundation.db.DockerPostgresContainer
import com.ktor_sample.foundation.e2e.prerequisite.E2ETestPrerequisites
import com.ktor_sample.foundation.e2e.prerequisite.E2EUser
import com.ktor_sample.modules.auth.UsernameAndPassword
import com.ktor_sample.plugins.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

internal open class KtorE2ETest {
    /** Custom client that appends the [sessionHeader] automatically and can handle json marshalling */
    protected lateinit var snakeCaseHttpClient: HttpClient

    /** Custom client that appends the [sessionHeader] automatically and can handle json marshalling */
    protected lateinit var camelCaseHttpClient: HttpClient

    /** Storing the session header here in order to reuse it in all requests made by [camelCaseHttpClient] */
    private var sessionHeader: String = ""

    /**
     * Helper method that performs a login and stores the returned sessionId.
     */

    /**
     * Helper method that performs a login and stores the returned sessionId.
     */
    private suspend fun loginWithCredentials(user: E2EUser): Result {
        val response = camelCaseHttpClient.post("/api/v1/auth/password-login") {
            contentType(ContentType.Application.Json)
            setBody(UsernameAndPassword(user.username, user.password))
        }

        // Adding this assertion here to make sure that tests fail fast if a login they require fails
        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        sessionHeader = response.headers["user_session"]!!
        return response.body()
    }

    /**
     * The actual testApp, initialised with the same values as the prod app. The main feature of this test app
     * is that is skips the http stack, and directly connects the test code to the endpoint implementations. This
     * makes the tests faster and more lightweight.
     *
     * Also initialises the [camelCaseHttpClient] which is used to perform http requests by tests.
     *
     * @param block The test implementation.
     */

    /**
     * The actual testApp, initialised with the same values as the prod app. The main feature of this test app
     * is that is skips the http stack, and directly connects the test code to the endpoint implementations. This
     * makes the tests faster and more lightweight.
     *
     * Also initialises the [camelCaseHttpClient] which is used to perform http requests by tests.
     *
     * @param block The test implementation.
     */
    protected fun testApp(
        prerequisites: E2ETestPrerequisites,
        httpMocks: (suspend ApplicationTestBuilder.() -> Unit)?,
        block: suspend (E2EUser?) -> Unit
    ) {
        /**
         * For some reason, if I try to migrate right after starting the postgres docker container I get an
         * error saying that the docker port is already bound. No idea why that's the case, and I have no time to waste
         * on this right now. So here's an ugly workaround
         */
        if (!DockerPostgresContainer.migrated) {
            val flywayMigrator: FlywayMigrator = DockerPostgresContainer.koinApp.koin.get()
            flywayMigrator.migrate()
            DockerPostgresContainer.migrated = true
        }

        prerequisites.setup(DockerPostgresContainer.koinApp.koin.get())
        testApplication {
            httpMocks?.let { it(this) }

            camelCaseHttpClient = createClient {
                install(DefaultRequest) {
                    header("user_session", sessionHeader)
                }
                install(ContentNegotiation) {
                    jackson {
                        propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
                        registerModules(JavaTimeModule())
                        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    }
                }
            }

            snakeCaseHttpClient = createClient {
                install(DefaultRequest) {
                    header("user_session", sessionHeader)
                }
                install(ContentNegotiation) {
                    jackson {
                        propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
                        registerModules(JavaTimeModule())
                        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    }
                }
            }

            application {
                configureDI(camelCaseHttpClient, snakeCaseHttpClient, DockerPostgresContainer.dataSource)
                configureSecurity()
                configureAuthentication()
                configureDocumentation()
                configureHTTP()
                configureMonitoring()
                configureSerialization()
                configureRouting()
                configureModules()
            }

            prerequisites.user?.let { loginWithCredentials(it) }
            try {
                block(prerequisites.user)
            } finally {
                prerequisites.tearDown(DockerPostgresContainer.koinApp.koin.get())
            }
        }
    }

    /**
     * Helper method that removes some boilerplate by allowing tests to omit the prerequisites param
     * and fallback to the default prerequisites.
     */

    /**
     * Helper method that removes some boilerplate by allowing tests to omit the prerequisites param
     * and fallback to the default prerequisites.
     */

    protected fun testApp(block: suspend (E2EUser?) -> Unit) = testApp(E2ETestPrerequisites.Default(), null, block)
    protected fun testApp(prerequisites: E2ETestPrerequisites, block: suspend (E2EUser?) -> Unit) =
        testApp(prerequisites, null, block)

    protected fun overrideTime(localDate: LocalDate) {
        loadKoinModules(module {
            single<Clock> {
                Clock.fixed(
                    localDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                    ZoneId.systemDefault()
                )
            }
        })
    }

    /**
     * We need to stop the Koin instance started by testApplication after each test, since the Ktor app uses
     * the Global Koin instance that's persisted across tests.
     */

    /**
     * We need to stop the Koin instance started by testApplication after each test, since the Ktor app uses
     * the Global Koin instance that's persisted across tests.
     */
    @AfterEach
    fun cleanUp() {
        stopKoin()
    }
}