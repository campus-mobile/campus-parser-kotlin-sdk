/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import kotlin.test.Test
import kotlin.test.assertFails

class AssertsTest {

    @Test
    fun validUrlAsserts() {
        listOf(
            "https://schedule.npi-tu.ru/api/v1/faculties",
            "https://schedule.npi-tu.ru/api/v1/faculties/2/years/1/groups/%D0%98%D0%A2%D0%B0/schedule",
            "https://docs.google.com/uc?export=download&id=1fGCHJRPFPFYdeKkp0frT14crxXHC_mTW"
        ).map { TestObject(url = it) }
            .forEach { assertUrl(it::url) }
    }

    @Test
    fun invalidUrlAsserts() {
        listOf(
            "https://schedule.npi-tu.ru/api/v1/faculties/2/years/1/groups/ИТа/schedule",
            "https:///docs.google.com",
            "://docs.google.com"
        ).map { TestObject(url = it) }
            .forEach {
                assertFails {
                    assertUrl(it::url)
                }
            }
    }

    data class TestObject(val url: String)
}
