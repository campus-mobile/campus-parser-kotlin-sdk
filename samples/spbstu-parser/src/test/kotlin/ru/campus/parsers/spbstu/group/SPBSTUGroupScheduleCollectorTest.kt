/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.group

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.respondOk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import ru.campus.parser.sdk.base.ScheduleCollector
import ru.campus.parser.sdk.model.Entity
import ru.campus.parsers.tests.sdk.readResourceFile
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SPBSTUGroupScheduleCollectorTest {
    private lateinit var collector: ScheduleCollector

    @BeforeTest
    fun setup() {
        val httpClient = HttpClient(MockEngine { request ->
            when (request.url.toString()) {
                "https://ruz.spbstu.ru/api/v1/ruz/scheduler/32373?date=2022-05-16" -> respondOk(
                    readResourceFile("spbstu/group/32373/group_32373_1week.json")
                )

                "https://ruz.spbstu.ru/api/v1/ruz/scheduler/32373?date=2022-05-23" -> respondOk(
                    readResourceFile("spbstu/group/32373/group_32373_2week.json")
                )

                "https://ruz.spbstu.ru/api/v1/ruz/scheduler/32373?date=2022-05-30" -> respondOk(
                    readResourceFile("spbstu/group/32373/group_32373_3week.json")
                )

                "https://ruz.spbstu.ru/api/v1/ruz/scheduler/32373?date=2022-06-06" -> respondOk(
                    readResourceFile("spbstu/group/32373/group_32373_4week.json")
                )

                else -> respondBadRequest()
            }
        })
        collector = SPBSTUGroupScheduleCollector(
            httpClient = httpClient,
            dateProvider = { LocalDateTime(year = 2022, monthNumber = 5, dayOfMonth = 16, hour = 0, minute = 0) }
        )
    }

    @Test
    fun `Расписание 4851001_00002`() = runTest {
        val result: ScheduleCollector.Result = collector.collectSchedule(
            entity = Entity(
                type = Entity.Type.Group,
                name = "4851001/00002",
                code = "32373",
                scheduleUrl = "https://ruz.spbstu.ru/faculty/122/groups/32373"
            ),
            intervals = emptyList()
        )


        assertEquals(
            expected = readResourceFile("spbstu/group/32373/entity_processed.txt"),
            actual = result.processedEntity.toString()
        )
        assertEquals(
            expected = readResourceFile("spbstu/group/32373/week_schedule_items.txt"),
            actual = result.weekScheduleItems.joinToString(separator = "\n")
        )
    }
}
