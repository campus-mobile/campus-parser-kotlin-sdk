/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.tests.sdk

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

interface MocksScope {
    fun respond(
        requestPredicate: (RecordedRequest, String) -> Boolean,
        responseCode: Int,
        responseBody: String,
        contentType: String? = null,
        requestChecker: (RecordedRequest, String) -> Unit = { _, _ -> },
    )

    fun respond(
        requestPredicate: (RecordedRequest, String) -> Boolean,
        process: (RecordedRequest) -> MockResponse,
    )
}

fun MocksScope.respond(
    requestUrl: String,
    responseCode: Int,
    responseBody: String,
    contentType: String? = null,
    requestChecker: (RecordedRequest, String) -> Unit = { _, _ -> },
) {
    respond(
        requestPredicate = { request, _ -> request.path.orEmpty() == requestUrl },
        responseCode = responseCode,
        responseBody = responseBody,
        contentType = contentType,
        requestChecker = requestChecker
    )
}
