/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import org.apache.logging.log4j.Logger

fun createDefaultHttpClient(
    logger: Logger,
    withProxy: Boolean = false,
    useHttp1: Boolean = false,
    withCookie: Boolean = true,
    socketTimeoutMillis: Long = 10000,
    requestTimeoutMillis: Long = 30000,
    configure: HttpClientConfig<OkHttpConfig>.() -> Unit = {},
): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            threadsCount = 8
            config {
                if (withCookie) cookieJar(InMemoryCookieJar())
                if (useHttp1) setHttpProtocol()
                if (withProxy) setupProxy()
                disableSslChecks()
            }
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
        install(HttpRequestRetry) {
            retryOnExceptionOrServerErrors(maxRetries = 3)
            exponentialDelay()
        }

        configure()
    }
}
