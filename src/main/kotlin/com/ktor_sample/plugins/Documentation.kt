package com.ktor_sample.plugins

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.bkbn.kompendium.core.attribute.KompendiumAttributes
import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.core.routes.redoc
import io.bkbn.kompendium.json.schema.SchemaConfigurator
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.info.Info
import io.bkbn.kompendium.oas.server.Server
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.URI
import java.time.Instant
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.typeOf

fun Application.configureDocumentation() {
    install(NotarizedApplication()) {
        spec = OpenApiSpec(
            info = Info("Ktor sample Main Backend", "0.1", "Monolith powering the Ktor sample mobile app", "Todo"),
            servers = mutableListOf(Server(URI("https://example.app")))
        )
        customTypes = mapOf(
            typeOf<Instant>() to TypeDefinition(type = "string", format = "date-time")
        )

        openApiJson = {
            authenticate("docs") {
                route("/openapi.json") {
                    get {
                        call.respond(
                            HttpStatusCode.OK,
                            this@route.application.attributes[KompendiumAttributes.openApiSpec]
                        )
                    }
                }
            }
        }

        schemaConfigurator = JacksonSchemaConfigurator()

        routing {
            authenticate("docs") {
                redoc(pageTitle = "Ktor sample API Docs", path = "/api-docs", "/openapi.json")
            }
        }
    }
}

// Adds support for JsonIgnore and JsonProperty annotations,
// if you are not using them this is not required
// This also does not support class level configuration
private class JacksonSchemaConfigurator : SchemaConfigurator.Default() {

    override fun serializableMemberProperties(clazz: KClass<*>): Collection<KProperty1<out Any, *>> =
        clazz.memberProperties
            .filterNot {
                it.hasJavaAnnotation<JsonIgnore>()
            }

    override fun serializableName(property: KProperty1<out Any, *>): String =
        property.getJavaAnnotation<JsonProperty>()?.value ?: property.name

}

private inline fun <reified T : Annotation> KProperty1<*, *>.hasJavaAnnotation(): Boolean {
    return javaField?.isAnnotationPresent(T::class.java) ?: false
}

private inline fun <reified T : Annotation> KProperty1<*, *>.getJavaAnnotation(): T? {
    return javaField?.getDeclaredAnnotation(T::class.java)
}