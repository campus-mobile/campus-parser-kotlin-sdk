/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parsers.tests.sdk.dump

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respondError
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodeURLParameter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import ru.campus.parser.sdk.api.ParserApi
import ru.campus.parser.sdk.model.Entity
import ru.campus.parser.sdk.utils.md5
import java.io.File
import kotlin.test.assertEquals

private const val DUMP_DIR_NAME = "dump-api"

fun createDumpRequestsParserApi(dumpDirName: String = DUMP_DIR_NAME) = ParserApi(
    httpClient = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                val url: String = request.url.toString()
                if (url.endsWith("entities")) {
                    saveRequestToDump(request, dumpDirName, hashInName = true)
                    responseEntitiesOk(request)
                } else if (url.endsWith("schedule")) {
                    saveRequestToDump(request, dumpDirName, hashInName = false)
                    responseScheduleOk()
                } else {
                    respondError(HttpStatusCode.BadRequest, "invalid url $url")
                }
            }
        }
    },
    baseUrl = "https://parser.api.campus.dewish.ru/v3",
    password = "",
    userName = ""
)

fun createDumpMockParserApi(dumpDirName: String = DUMP_DIR_NAME) = ParserApi(
    httpClient = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                val url: String = request.url.toString()
                if (url.endsWith("entities")) {
                    if (isRequestDumpExist(request, dumpDirName)) {
                        responseEntitiesOk(request)
                    } else {
                        respondError(HttpStatusCode.BadRequest, "$request not found in dump")
                    }
                } else if (url.endsWith("schedule")) {
                    val dump = getRequestDump(request, dumpDirName)
                    val body = request.bodyText().prettyJson()

                    assertEquals(expected = dump, actual = body)

                    responseScheduleOk()
                } else {
                    respondError(HttpStatusCode.BadRequest, "invalid url $url")
                }
            }
        }
    },
    baseUrl = "https://parser.api.campus.dewish.ru/v3",
    password = "",
    userName = ""
)

private val prettyJson = Json { prettyPrint = true }

private suspend fun MockRequestHandleScope.responseEntitiesOk(request: HttpRequestData): HttpResponseData {
    val id: Entity = prettyJson.decodeFromString(request.bodyText())
    return respondOk("""{"_id":"${id.code}","code":"","name":"","status":"new"}""")
}

private fun MockRequestHandleScope.responseScheduleOk(): HttpResponseData {
    return respondOk("""{"new":1,"updated":1}""")
}

private suspend fun HttpRequestData.bodyText(): String {
    return body.toByteArray().toString(Charsets.UTF_8)
}

private fun String.prettyJson(): String {
    val jsonElement: JsonElement = prettyJson.decodeFromString(JsonElement.serializer(), this)
    return prettyJson.encodeToString(jsonElement)
}

private suspend fun saveRequestToDump(request: HttpRequestData, dumpDirName: String, hashInName: Boolean) {
    val method: String = request.method.value
    val url: String = request.url.toString().encodeURLParameter()
    val body: String = request.bodyText()

    val fileName: String = if (hashInName) {
        val bodyHash: String = body.md5()
        "$method-$url-$bodyHash.json"
    } else {
        "$method-$url.json"
    }

    val dir = File(dumpDirName)
    dir.mkdirs()
    val file = File(dir, fileName)
    file.writeText(body.prettyJson())
}

private suspend fun isRequestDumpExist(request: HttpRequestData, dumpDirName: String): Boolean {
    val method: String = request.method.value
    val url: String = request.url.toString().encodeURLParameter()
    val body: String = request.bodyText()
    val bodyHash: String = body.md5()

    val fileName = "$method-$url-$bodyHash.json"

    val dir = File(dumpDirName)
    dir.mkdirs()
    val file = File(dir, fileName)
    return file.exists()
}

private fun getRequestDump(request: HttpRequestData, dumpDirName: String): String {
    val method: String = request.method.value
    val url: String = request.url.toString().encodeURLParameter()

    val fileName = "$method-$url.json"

    val dir = File(dumpDirName)
    dir.mkdirs()
    val file = File(dir, fileName)
    return file.readText()
}
