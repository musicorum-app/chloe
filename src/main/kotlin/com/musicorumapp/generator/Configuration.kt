package com.musicorumapp.generator

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Serializable
data class Worker(
    val name: String
)

@Serializable
data class Config(
    val port: Short,
    val worker: Worker,
    @SerialName("api_key")
    val apiKey: String,
    @SerialName("enabled_themes")
    val supportedThemes: List<String>
)

@Serializable
data class Engine(
    val name: String,
    val slug: String,
    val version: Float,
    val scheme: Float
)

class Configuration {
    private val configFile = File("./config.yaml")
    private fun getDefaultConfigFile(file: String): File {
        val path = ClassLoader.getSystemClassLoader().getResource(file)
        if (path != null)
            return File(path.file)
        else throw Exception("Default config file not found.")
    }

    private fun readFile(): String {
        if (!configFile.exists()) {
            val default = getDefaultConfigFile("config.yaml")
            Files.copy(default.toPath(), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        return configFile.readText(Charsets.UTF_8)
    }

    fun getConfig(): Config {
        return Yaml.default.parse(Config.serializer(), readFile())
    }

    fun getEngineConfig(): Engine {
        val content = getDefaultConfigFile("engine.yaml").readText(Charsets.UTF_8)
        return Yaml.default.parse(Engine.serializer(), content)
    }
}