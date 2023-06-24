/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import ru.campus.parser.sdk.Logger

fun createDefaultHttpClient(
    logger: Logger,
    withCookie: Boolean = true,
    socketTimeoutMillis: Long = 10000,
    requestTimeoutMillis: Long = 30000,
    engine: HttpClientEngine = createDefaultHttpClientEngine(),
    configure: HttpClientConfig<*>.() -> Unit = {},
): HttpClient {
    return HttpClient(engine) {
        if (withCookie) {
            install(HttpCookies)
        }

        install(HttpTimeout) {
            this.socketTimeoutMillis = socketTimeoutMillis
            this.requestTimeoutMillis = requestTimeoutMillis
        }
        install(Logging) {
            this.level = LogLevel.INFO
            this.logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    logger.info(message)
                }
            }
        }

        expectSuccess = true

        configure()
    }
}
