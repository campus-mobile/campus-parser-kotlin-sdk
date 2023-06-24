/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import okhttp3.internal.http2.ConnectionShutdownException
import okhttp3.internal.http2.StreamResetException
import java.net.SocketException
import java.net.SocketTimeoutException

actual val Throwable.isRetryAllowed: Boolean
    get() = when (this) {
        is SocketException,
        is SocketTimeoutException,
        is HttpRequestTimeoutException,
        is ClientRequestException,
        is ServerResponseException,
        is StreamResetException,
        is ConnectionShutdownException,
        -> true

        else -> false
    }
