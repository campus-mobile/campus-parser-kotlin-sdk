/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parsers.tests.sdk.dump

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.client.engine.mock.respondOk
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodeURLParameter
import java.io.File

private const val DUMP_DIR_NAME = "dump"

fun createDumpRequestsHttpClient(dumpDirName: String = DUMP_DIR_NAME): HttpClient {
    return HttpClient {
        install(DumpRequests) {
            dirName = dumpDirName
        }
    }
}

fun createDumpMockHttpClient(dumpDirName: String = DUMP_DIR_NAME): HttpClient {
    return HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                val url: String = request.url.toString().encodeURLParameter()
                val method: String = request.method.value
                val bodyHash: String = request.body.hash()

                val fileName = "$method-$url-$bodyHash.resp"

                val dir = File(dumpDirName)
                val file = File(dir, fileName)

                if (file.exists()) {
                    val body = file.readText()
                    respondOk(body)
                } else {
                    respondError(HttpStatusCode.BadRequest, "$request dump not found")
                }
            }
        }
    }
}
