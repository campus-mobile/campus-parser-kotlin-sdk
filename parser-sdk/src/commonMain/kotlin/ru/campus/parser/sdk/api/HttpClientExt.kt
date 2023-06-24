/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay
import ru.campus.parser.sdk.Logger
import ru.campus.parser.sdk.utils.isRetryAllowed
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
        if (exc.isRetryAllowed) {
            logger.warn(message = "Socket timeout, trying again. Try counter: {}", exc = exc, counter)
            if (counter == 3) throw exc
            delay((30 * counter).seconds)

            requestWithRetry(logger, counter + 1, request)
        } else {
            throw RequestWithRetryFailedException(exc)
        }
    }
}

class RequestWithRetryFailedException(cause: Throwable) : IllegalStateException(cause)
