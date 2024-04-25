package com.kevalkanpariya

import com.kevalkanpariya.data.local.DatabaseSingleton
import com.kevalkanpariya.plugins.*
import com.kevalkanpariya.service.features.ConsulFeature
import com.orbitz.consul.Consul
import com.orbitz.consul.model.agent.ImmutableRegistration
import io.ktor.client.*
import io.ktor.client.engine.apache5.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {


    val server = embeddedServer(Netty, port = 8090, host = "127.0.0.1", module = Application::module)

    val consulClient = Consul.builder().withUrl("http://localhost:8500").build()
    val service = ImmutableRegistration.builder()
        .id("product-${server.environment.connectors[0].port}")
        .name("product-service")
        .address("localhost")
        .port(server.environment.connectors[0].port)
        .build()
    consulClient.agentClient().register(service)

        server.start(wait = true)
}

fun Application.module() {
    DatabaseSingleton.init(environment.config)

    environment.config.config("")

    val client = HttpClient(Apache5) {
        install(ContentNegotiation) {
            json() // Example: Register JSON content transformation
            // Add more transformations as needed for other content types
        }
        install(ConsulFeature) {
            consulUrl = "http://localhost:8500"
        }

    }

    configureSecurity()
    configureSerialization()
    configureRouting(client)
    configureTemplating()
}
