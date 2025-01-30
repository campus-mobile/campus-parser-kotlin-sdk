/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalDateExtKtTest {

    @Test
    fun testWeekOfYear() {
        // Недели считаются от той недели, на которую выпало 4 января. (международный стандарт)
        // Можно брать значения с гугл-календаря, он следует тому же стандарту.
        for (i in 1..2) {
            assertWeek(weekNumber = 52, date = LocalDate(year = 2022, monthNumber = 1, dayOfMonth = i))
        }
        for (i in 3..9) {
            assertWeek(weekNumber = 1, date = LocalDate(year = 2022, monthNumber = 1, dayOfMonth = i))
        }
        for (i in 10..16) {
            assertWeek(weekNumber = 2, date = LocalDate(year = 2022, monthNumber = 1, dayOfMonth = i))
        }
        for (i in 17..23) {
            assertWeek(weekNumber = 3, date = LocalDate(year = 2022, monthNumber = 1, dayOfMonth = i))
        }
        for (i in 24..30) {
            assertWeek(weekNumber = 4, date = LocalDate(year = 2022, monthNumber = 1, dayOfMonth = i))
        }
        for (i in 1..6) {
            assertWeek(weekNumber = 5, date = LocalDate(year = 2022, monthNumber = 2, dayOfMonth = i))
        }
        for (i in 7..13) {
            assertWeek(weekNumber = 6, date = LocalDate(year = 2022, monthNumber = 2, dayOfMonth = i))
        }
        for (i in 14..20) {
            assertWeek(weekNumber = 7, date = LocalDate(year = 2022, monthNumber = 2, dayOfMonth = i))
        }
        for (i in 21..27) {
            assertWeek(weekNumber = 8, date = LocalDate(year = 2022, monthNumber = 2, dayOfMonth = i))
        }
        for (i in 1..6) {
            assertWeek(weekNumber = 9, date = LocalDate(year = 2022, monthNumber = 3, dayOfMonth = i))
        }
        assertWeek(weekNumber = 52, date = LocalDate(year = 2023, monthNumber = 1, dayOfMonth = 1))
        for (i in 2..8) {
            assertWeek(weekNumber = 1, date = LocalDate(year = 2023, monthNumber = 1, dayOfMonth = i))
        }
        for (i in 1..7) {
            assertWeek(weekNumber = 1, date = LocalDate(year = 2024, monthNumber = 1, dayOfMonth = i))
        }
        for (i in 30..31) {
            assertWeek(weekNumber = 1, date = LocalDate(year = 2024, monthNumber = 12, dayOfMonth = i))
        }
        for (i in 1..5) {
            assertWeek(weekNumber = 1, date = LocalDate(year = 2025, monthNumber = 1, dayOfMonth = i))
        }
        for (i in 29..31) {
            assertWeek(weekNumber = 1, date = LocalDate(year = 2025, monthNumber = 12, dayOfMonth = i))
        }
        for (i in 28..31) {
            assertWeek(weekNumber = 53, date = LocalDate(year = 2026, monthNumber = 12, dayOfMonth = i))
        }
        for (i in 1..3) {
            assertWeek(weekNumber = 53, date = LocalDate(year = 2027, monthNumber = 1, dayOfMonth = i))
        }
    }

    private fun assertWeek(weekNumber: Int, date: LocalDate) {
        assertEquals(
            expected = weekNumber,
            actual = date.weekNumber,
            message = "Week number for $date invalid"
        )
    }

    @Test
    fun weeksBetween() {
        val start = LocalDate(year = 2022, monthNumber = 2, dayOfMonth = 7)
        assertEquals(expected = 1, actual = start.weeksUntil(LocalDate(year = 2022, monthNumber = 2, dayOfMonth = 8)))
        assertEquals(expected = 1, actual = start.weeksUntil(LocalDate(year = 2022, monthNumber = 2, dayOfMonth = 13)))
        assertEquals(expected = 2, actual = start.weeksUntil(LocalDate(year = 2022, monthNumber = 2, dayOfMonth = 14)))
    }
}
