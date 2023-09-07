/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay
import okhttp3.internal.http2.ConnectionShutdownException
import okhttp3.internal.http2.StreamResetException
import org.apache.logging.log4j.Logger
import java.io.IOException
import java.net.SocketException
import kotlin.time.Duration.Companion.seconds

suspend fun HttpClient.requestWithRetry(
    logger: Logger,
    counter: Int = 0,
    request: HttpRequestBuilder.() -> Unit,
): HttpResponse {
    return try {
        val requestBuilder: HttpRequestBuilder = HttpRequestBuilder().apply(request)
        logger.info("Send request {} counter {}", requestBuilder.url.buildString(), counter)
        this.request(requestBuilder)
    } catch (exc: Exception) {
        when (exc) {
            is SocketException,
            is SocketTimeoutException,
            is HttpRequestTimeoutException,
            is ClientRequestException,
            is ServerResponseException,
            is StreamResetException,
            is ConnectionShutdownException,
            -> {
                logger.warn("Socket timeout, trying again. Try counter: {}", counter)
                logger.warn(exc.localizedMessage, exc)
                if (counter == 3) throw exc
                delay((30 * counter).seconds)

                requestWithRetry(logger, counter + 1, request)
            }

            else -> throw RequestWithRetryFailedException(exc)
        }
    }
}

class RequestWithRetryFailedException(cause: Throwable) : IOException(cause)
