/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import java.util.logging.Level
import java.util.logging.Logger

fun createDefaultHttpClient(
    logger: Logger,
    withProxy: Boolean = false,
    configure: HttpClientConfig<OkHttpConfig>.() -> Unit = {},
): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            threadsCount = 8
            config {
                cookieJar(InMemoryCookieJar())
                if (withProxy) setupProxy()
                if (withProxy) setupAuthenticator()
                disableSslChecks()
            }
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 10000
            requestTimeoutMillis = 30000
        }
        install(Logging) {
            this.level = LogLevel.INFO
            this.logger = object : io.ktor.client.features.logging.Logger {
                override fun log(message: String) {
                    logger.log(Level.INFO, message)
                }
            }
        }
        configure()
    }
}
