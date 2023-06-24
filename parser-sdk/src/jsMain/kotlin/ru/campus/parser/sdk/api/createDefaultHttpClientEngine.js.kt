/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.js.Js

actual fun createDefaultHttpClientEngine(
    withProxy: Boolean,
    useHttp1: Boolean,
    config: HttpClientEngineConfig.() -> Unit
): HttpClientEngine {
    return Js.create {
        threadsCount = 8

        // TODO other jvm supported options
    }
}
