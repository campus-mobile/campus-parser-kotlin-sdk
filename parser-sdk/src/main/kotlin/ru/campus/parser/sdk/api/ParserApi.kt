/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.pathComponents
import io.ktor.http.takeFrom
import io.ktor.util.InternalAPI
import io.ktor.util.encodeBase64
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.campus.parser.sdk.model.Entity
import ru.campus.parser.sdk.model.EntityResult
import ru.campus.parser.sdk.model.Schedule
import ru.campus.parser.sdk.model.ScheduleResult

class ParserApi(
    private val json: Json = Json,
    private val httpClient: HttpClient,
    private val baseUrl: String,
    userName: String,
    password: String,
) {

    private val authHeader: String = "$userName:$password".encodeBase64()

    @OptIn(InternalAPI::class)
    suspend fun sendEntity(entity: Entity): EntityResult {
        val response: HttpResponse = httpClient.post {
            url {
                takeFrom(baseUrl)
                pathComponents("entities")
            }

            header("Authorization", "Basic $authHeader")
            header("Content-Type", "application/json")

            body = json.encodeToString(Entity.serializer(), entity)
            expectSuccess = false
        }
        val status: HttpStatusCode = response.status
        val body = response.body<String>()

        ensureStatusCodeOk(response.request, status, body)

        return json.decodeFromString(EntityResult.serializer(), body)
    }

    @OptIn(InternalAPI::class)
    suspend fun sendSchedule(entityId: String, schedules: List<Schedule>): ScheduleResult {
        val response: HttpResponse = httpClient.post {
            url {
                takeFrom(baseUrl)
                pathComponents("entities", entityId, "schedule")
            }

            header("Authorization", "Basic $authHeader")
            header("Content-Type", "application/json")

            body = json.encodeToString(ListSerializer(Schedule.serializer()), schedules)
            expectSuccess = false
        }
        val status: HttpStatusCode = response.status
        val body = response.body<String>()

        ensureStatusCodeOk(response.request, status, body)

        return json.decodeFromString(ScheduleResult.serializer(), body)
    }

    private fun ensureStatusCodeOk(request: HttpRequest, status: HttpStatusCode, body: String) {
        if (status == HttpStatusCode.OK) return

        throw InvalidStatusCodeException(
            request = request,
            status = status,
            body = body
        )
    }

    class InvalidStatusCodeException(
        val request: HttpRequest,
        val status: HttpStatusCode,
        val body: String,
    ) : IllegalStateException("$request got invalid status $status with body $body")
}
