/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.group

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.engine.mock.respondOk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.campus.parser.sdk.base.EntitiesCollector
import ru.campus.parser.sdk.model.Entity
import ru.campus.parsers.tests.sdk.readResourceFile
import kotlin.test.BeforeTest
import kotlin.test.assertEquals


class SPBSTUGroupEntitiesCollectorTest {

    private lateinit var collector: EntitiesCollector

    @BeforeTest
    fun setup() {
        collector = SPBSTUGroupEntitiesCollector(
            HttpClient(MockEngine { request ->
                when (request.url.toString()) {
                    "https://ruz.spbstu.ru/" -> respondOk(
                        readResourceFile("spbstu/ruz_spbstu_page.html")
                    )

                    "https://ruz.spbstu.ru/api/v1/ruz/faculties/122/groups" -> respondOk(
                        readResourceFile("spbstu/group/faculty_122_groups.json")
                    )

                    else -> respondBadRequest()
                }
            })
        )
    }

    @Test
    fun `success read faculty`() = runTest {
        val groups: List<Entity> = collector.collectEntities()

        assertEquals(
            expected = 40,
            actual = groups.size
        )
    }
}
