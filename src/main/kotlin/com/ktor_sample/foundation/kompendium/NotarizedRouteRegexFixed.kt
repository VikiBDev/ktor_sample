package com.ktor_sample.foundation.kompendium

import io.bkbn.kompendium.core.attribute.KompendiumAttributes
import io.bkbn.kompendium.core.metadata.*
import io.bkbn.kompendium.core.util.Helpers.addToSpec
import io.bkbn.kompendium.core.util.SpecConfig
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.path.Path
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlin.collections.set

/**
 * Monkey patch for the NotarizedRouteRegex class that is bugged in the Notarized library. See line 82 for the actual
 * patch.
 */
object NotarizedRouteRegexFixed {
    class Config : SpecConfig {
        override var tags: Set<String> = emptySet()
        override var parameters: List<Parameter> = emptyList()
        override var get: GetInfo? = null
        override var post: PostInfo? = null
        override var put: PutInfo? = null
        override var delete: DeleteInfo? = null
        override var patch: PatchInfo? = null
        override var head: HeadInfo? = null
        override var options: OptionsInfo? = null
        override var security: Map<String, List<String>>? = null
    }

    private object InstallHook : Hook<(ApplicationCallPipeline) -> Unit> {
        override fun install(pipeline: ApplicationCallPipeline, handler: (ApplicationCallPipeline) -> Unit) {
            handler(pipeline)
        }
    }

    operator fun invoke() = createRouteScopedPlugin(
        name = "NotarizedRoute",
        createConfiguration = NotarizedRouteRegexFixed::Config
    ) {
        // This is required in order to introspect the route path and authentication
        on(InstallHook) {
            val route = it as? Route ?: return@on
            val spec = application.attributes[KompendiumAttributes.openApiSpec]
            val routePath = route.calculateRoutePath()
            val authMethods = route.collectAuthMethods()

            addToSpec(spec, routePath, authMethods)
        }
    }

    fun <T : SpecConfig> PluginBuilder<T>.addToSpec(
        spec: OpenApiSpec,
        fullPath: String,
        authMethods: List<String>
    ) {
        val path = spec.paths[fullPath] ?: Path()

        path.parameters = path.parameters?.plus(pluginConfig.parameters) ?: pluginConfig.parameters
        val serializableReader = application.attributes[KompendiumAttributes.schemaConfigurator]

        pluginConfig.get?.addToSpec(path, spec, pluginConfig, serializableReader, fullPath, authMethods)
        pluginConfig.delete?.addToSpec(path, spec, pluginConfig, serializableReader, fullPath, authMethods)
        pluginConfig.head?.addToSpec(path, spec, pluginConfig, serializableReader, fullPath, authMethods)
        pluginConfig.options?.addToSpec(path, spec, pluginConfig, serializableReader, fullPath, authMethods)
        pluginConfig.post?.addToSpec(path, spec, pluginConfig, serializableReader, fullPath, authMethods)
        pluginConfig.put?.addToSpec(path, spec, pluginConfig, serializableReader, fullPath, authMethods)
        pluginConfig.patch?.addToSpec(path, spec, pluginConfig, serializableReader, fullPath, authMethods)

        spec.paths[fullPath] = path
    }

    private fun Route.calculateRoutePath() = toString()
        .let {
            application.environment.rootPath.takeIf { root -> root.isNotEmpty() }
                ?.let { root ->
                    val sanitizedRoute = if (root.startsWith("/")) root else "/$root"
                    it.replace(sanitizedRoute, "")
                }
                ?: it
        }
        /**
         * Monkey patch, the last regex is too aggressive and removes too much info from the path.
         * We need to first remove the "method" group, and then remove the rest of the parenthesis
         */
        .replace(Regex("\\(method.+\\)"), "")
        .replace(Regex("\\(.+\\)\\/"), "")

    private fun Route.collectAuthMethods() = toString()
        .split("/")
        .filter { it.contains(Regex("\\(authenticate .*\\)")) }
        .map { it.replace("(authenticate ", "").replace(")", "") }
        .map { it.split(", ") }
        .flatten()
}
