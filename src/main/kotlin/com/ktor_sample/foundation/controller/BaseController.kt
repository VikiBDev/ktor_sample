package com.ktor_sample.foundation.controller

import com.ktor_sample.foundation.Language
import com.ktor_sample.foundation.Result
import com.ktor_sample.foundation.controller.exception.core.BaseException
import com.ktor_sample.foundation.controller.exception.core.LoudException
import com.ktor_sample.foundation.controller.exception.core.SilentException
import com.ktor_sample.foundation.controller.requestcontext.KtorSampleEnvironment
import com.ktor_sample.foundation.controller.requestcontext.RequestContext
import com.ktor_sample.foundation.http.Empty
import com.ktor_sample.foundation.toResult
import com.ktor_sample.foundation.userId
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*


/**
 * OMG what's going on in this class??
 *
 * Well, glad you asked. The art of programming often requires trade-offs, and this is one of them. It's not ideal
 * to require every controller to define its input type (even if there's no body input, and in that case Empty should
 * be used), and it's not nice to horribly crash if a controller defines a non-Empty input class, and then passes null
 * as the second param to the execute function.
 *
 * If Kotlin adds support for reified class generics in the future, we can remove the requestBody param
 * from execute and directly parse the request body here. Until then, this is what we have to deal with.
 */
const val defaultLanguage = "en"
const val X_CLIENT_LANGUAGE_HEADER = "x-client-language"

abstract class BaseController<Input>(private val environment: KtorSampleEnvironment) {
    suspend fun execute(call: ApplicationCall, requestBody: Input? = null) {
        val languageCode = call.request.header(X_CLIENT_LANGUAGE_HEADER) ?: defaultLanguage
        val context =
            RequestContext(call, environment, Language(languageCode, defaultLanguage), requestBody ?: Empty as Input)
        try {
            val response = handle(context)
            if (response is Unit)
                context.call.respond(Empty.toResult())
            else {
                context.call.respond(response.toResult())
            }

        } catch (e: BaseException) {
            environment.logger.error(
                "userId=${call.userId()};traceId=${e.traceId};errorCode:${e.errorCode};message=${e.message}",
                e.originalException
            )
            when (e) {
                is LoudException -> call.respond(Result.Error(e.message, e.traceId, "alert", e.title))
                is SilentException -> call.respond(Result.Error(e.message, e.traceId, e.errorCode))
            }
        } catch (e: Exception) {
            val traceId = UUID.randomUUID()
            environment.logger.error("[UNCAUGHT]: userId=${call.userId()};traceId=$traceId", e)
            context.call.respond(
                Result.Error(
                    "common.network.error",
                    UUID.randomUUID(),
                    "uncaught_exception",
                    "Unhandled error"
                )
            )
        }
    }

    protected abstract suspend fun handle(context: RequestContext<Input>): Any
}
