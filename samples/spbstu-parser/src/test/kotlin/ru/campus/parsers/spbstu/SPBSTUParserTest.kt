/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.respondOk
import io.ktor.http.content.TextContent
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import ru.campus.parser.sdk.model.Credentials
import ru.campus.parsers.tests.sdk.ThrowableLogger
import ru.campus.parsers.tests.sdk.readResourceFile
import ru.campus.parsers.tests.sdk.singleLineJson
import kotlin.test.Test
import kotlin.test.assertEquals

class SPBSTUParserTest {
    @Test
    fun `4831001_00001 success`() = runTest {
        val parser = SPBSTUParser(
            credentials = Credentials("", ""),
            parserApiBaseUrl = "https://parser.api.campus.dewish.ru/v3",
            httpClient = HttpClient(MockEngine { request ->
                when (request.url.toString()) {
                    "https://ruz.spbstu.ru/" -> respondOk(
                        readResourceFile("spbstu/ruz_spbstu_page.html")
                    )

                    "https://ruz.spbstu.ru/api/v1/ruz/faculties/122/groups" -> respondOk(
                        readResourceFile("spbstu/group/32369/faculty_122_group_4831001_00001.json")
                    )

                    "https://ruz.spbstu.ru/api/v1/ruz/scheduler/32369?date=2022-05-16" -> respondOk(
                        readResourceFile("spbstu/group/32369/32369_schedule_1week.json")
                    )

                    "https://ruz.spbstu.ru/api/v1/ruz/scheduler/32369?date=2022-05-23" -> respondOk(
                        readResourceFile("spbstu/group/32369/32369_schedule_2week.json")
                    )

                    "https://ruz.spbstu.ru/api/v1/ruz/scheduler/32369?date=2022-05-30" -> respondOk(
                        readResourceFile("spbstu/group/32369/32369_schedule_3week.json")
                    )

                    "https://ruz.spbstu.ru/api/v1/ruz/scheduler/32369?date=2022-06-06" -> respondOk(
                        readResourceFile("spbstu/group/32369/32369_schedule_4week.json")
                    )

                    "https://parser.api.campus.dewish.ru/v3/entities" -> {
                        val body = request.body as TextContent
                        if (body.text.contains("32369")) {
                            assertEquals(
                                expected = readResourceFile("spbstu/group/32369/entity_request.json").singleLineJson(),
                                actual = body.text
                            )
                            respondOk(readResourceFile("spbstu/group/entity_response.json"))
                        } else if (body.text.contains("17189")) {
                            assertEquals(
                                expected = readResourceFile("spbstu/group/32369/teacher_entity_request.json").singleLineJson(),
                                actual = body.text
                            )
                            respondOk(readResourceFile("spbstu/group/teacher_entity_response.json"))
                        } else {
                            respondBadRequest()
                        }
                    }

                    "https://parser.api.campus.dewish.ru/v3/entities/teacherid/schedule" -> {
                        val body = request.body as TextContent
                        assertEquals(
                            expected = readResourceFile("spbstu/group/32369/teacher_schedule_request.json").singleLineJson(),
                            actual = body.text
                        )
                        respondOk(readResourceFile("spbstu/group/group_schedule_response.json"))
                    }

                    "https://parser.api.campus.dewish.ru/v3/entities/groupid/schedule" -> {
                        val body = request.body as TextContent
                        assertEquals(
                            expected = readResourceFile("spbstu/group/32369/schedule_request.json").singleLineJson(),
                            actual = body.text
                        )
                        respondOk(readResourceFile("spbstu/group/group_schedule_response.json"))
                    }

                    else -> respondBadRequest()
                }
            }),
            dateProvider = { LocalDateTime(year = 2022, monthNumber = 5, dayOfMonth = 16, hour = 0, minute = 0) },
            logger = ThrowableLogger()
        )
        val result = parser.parse()
        assertEquals(
            expected = 1,
            actual = result.entitiesWithLesson
        )
        assertEquals(
            expected = 28,
            actual = result.savedSchedules.single().schedule.size
        )
    }
}
