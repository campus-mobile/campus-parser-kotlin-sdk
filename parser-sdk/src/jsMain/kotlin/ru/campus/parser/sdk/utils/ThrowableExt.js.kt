/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException

actual val Throwable.isRetryAllowed: Boolean
    get() = when (this) {
        is SocketTimeoutException,
        is HttpRequestTimeoutException,
        is ClientRequestException,
        is ServerResponseException,
        -> true

        else -> false
    }
