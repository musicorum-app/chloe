package com.musicorumapp.generator.routing

import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

interface Router {
    suspend fun handleGet(ctx: PipelineContext<Unit, ApplicationCall>) {
    }

    fun handlePost(ctx: PipelineContext<Unit, ApplicationCall>) {

    }
}