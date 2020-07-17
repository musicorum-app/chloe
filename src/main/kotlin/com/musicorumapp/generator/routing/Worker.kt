package com.musicorumapp.generator.routing

import com.musicorumapp.generator.Configuration
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.util.pipeline.PipelineContext
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.json
import kotlinx.serialization.stringify

@Serializable
data class WorkerResponse(
    val name: String,
    val engine: String,
    val version: Float,
    val scheme: Float,
    val themes: List<String>
)

class Worker: Router {
    override suspend fun handleGet(ctx: PipelineContext<Unit, ApplicationCall>) {
        val config = Configuration().getConfig()
        val engine = Configuration().getEngineConfig()
        val json = Json(JsonConfiguration.Stable)
        val data = WorkerResponse(
            config.worker.name,
            engine.slug,
            engine.version,
            engine.scheme,
            config.supportedThemes
        )
        val res = json.stringify(WorkerResponse.serializer(), data)
        ctx.call.respondText(res, ContentType.Application.Json)
    }
}