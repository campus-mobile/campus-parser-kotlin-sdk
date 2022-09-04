/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.tests.sdk

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

fun MockWebServer.mocks(mockBody: MocksScope.() -> Unit) {
    val mocksList: MutableList<Pair<(RecordedRequest, String) -> Boolean, (RecordedRequest, String) -> MockResponse>> =
        mutableListOf()
    // fill mocks list
    object : MocksScope {
        override fun respond(
            requestPredicate: (RecordedRequest, String) -> Boolean,
            responseCode: Int,
            responseBody: String,
            contentType: String?,
            requestChecker: (RecordedRequest, String) -> Unit,
        ) {
            val action: (RecordedRequest, String) -> MockResponse = { request, body ->
                try {
                    requestChecker(request, body)

                    MockResponse()
                        .setResponseCode(responseCode)
                        .apply {
                            if (contentType != null) setHeader("Content-Type", contentType)
                        }
                        .setBody(responseBody)
                } catch (exc: Throwable) {
                    MockResponse().setResponseCode(500).setBody(exc.toString())
                }
            }
            mocksList.add(requestPredicate to action)
        }

        override fun respond(
            requestPredicate: (RecordedRequest, String) -> Boolean,
            process: (RecordedRequest) -> MockResponse,
        ) {
            val action: (RecordedRequest, String) -> MockResponse = { request, body ->
                try {
                    process(request)
                } catch (exc: Throwable) {
                    MockResponse().setResponseCode(500).setBody(exc.toString())
                }
            }
            mocksList.add(requestPredicate to action)
        }
    }.mockBody()
    // create dispatcher with all mocks
    dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val body: String = request.body.readUtf8()
            val action = mocksList.firstOrNull { it.first(request, body) }

            val mockResponse = action?.second?.invoke(request, body)
            @Suppress("IfThenToElvis")
            return if (mockResponse != null) {
                mockResponse
            } else {
                MockResponse().setResponseCode(404)
            }
        }
    }
}

fun createMockHttpClient(port: Int): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest: Request = chain.request()
            val newRequest: Request = originalRequest.newBuilder()
                .url(
                    originalRequest.url.newBuilder()
                        .scheme("http")
                        .host("localhost")
                        .port(port)
                        .encodedPath("/" + originalRequest.url.host + originalRequest.url.encodedPath)
                        .build()
                )
                .build()
            chain.proceed(newRequest)
        }
        .build()
}
