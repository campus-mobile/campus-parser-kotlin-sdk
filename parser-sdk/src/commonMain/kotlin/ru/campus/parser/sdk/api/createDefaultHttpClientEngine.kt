/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig

expect fun createDefaultHttpClientEngine(
    withProxy: Boolean = false,
    useHttp1: Boolean = false,
    config: HttpClientEngineConfig.() -> Unit = {}
): HttpClientEngine
