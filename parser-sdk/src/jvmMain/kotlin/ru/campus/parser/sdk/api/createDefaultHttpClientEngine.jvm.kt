/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttp

actual fun createDefaultHttpClientEngine(
    withProxy: Boolean,
    useHttp1: Boolean,
    config: HttpClientEngineConfig.() -> Unit
): HttpClientEngine {
    return OkHttp.create {
        threadsCount = 8

        config {
            if (useHttp1) setHttpProtocol()
            if (withProxy) setupProxy()
            if (withProxy) setupAuthenticator()
            disableSslChecks()
        }
    }
}
