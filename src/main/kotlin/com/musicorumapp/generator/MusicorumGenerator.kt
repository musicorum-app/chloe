package com.musicorumapp.generator

import com.musicorumapp.generator.routing.Worker
import com.musicorumapp.generator.themes.Grid
import com.musicorumapp.generator.utils.CYAN
import com.musicorumapp.generator.utils.RESET
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondOutputStream
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import javax.imageio.ImageIO

class MusicorumGenerator {
    lateinit var config: Config
    lateinit var engine: Engine
    fun main(){
        config = Configuration().getConfig()
        engine = Configuration().getEngineConfig()

        println("Booting up...")
        println("Starting worker $CYAN${config.worker.name} ${engine.version}"
        + "$RESET using $CYAN${engine.name} ${engine.version}"
        + "$RESET with Generator Scheme $CYAN${engine.scheme}$RESET.")
        server()
    }

    private fun server() {
        println("Serving http server at $CYAN:${config.port}$RESET")
        embeddedServer(Netty, config.port.toInt()) {
            routing {
                get ("/worker") {
                    Worker().handleGet(this)
                }

                get("/") {
                    try {
                        val img = call.request.queryParameters["user"]?.let { it1 -> Grid().generate(it1) }
                        call.respondOutputStream(ContentType.Image.JPEG, HttpStatusCode.OK, producer = {
                            ImageIO.write(img, "jpg", this)
                        })
                    } catch (err: Exception) {
                        System.err.println(err)
                        System.err.println(err.stackTrace)
                    }
                }
            }
        }.start(wait = false)
    }
}

fun main() {
    MusicorumGenerator().main()
}