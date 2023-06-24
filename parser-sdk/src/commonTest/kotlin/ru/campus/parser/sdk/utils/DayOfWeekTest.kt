/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import kotlinx.datetime.DayOfWeek
import kotlin.test.Test
import kotlin.test.assertEquals

class DayOfWeekTest {
    @Test
    fun checkDay() {
        assertEquals(
            expected = DayOfWeek.MONDAY,
            actual = "понедельник".toDayOfWeekFullNameOrNull()
        )
        assertEquals(
            expected = DayOfWeek.TUESDAY,
            actual = "вторник".toDayOfWeekFullNameOrNull()
        )
        assertEquals(
            expected = DayOfWeek.WEDNESDAY,
            actual = "среда".toDayOfWeekFullNameOrNull()
        )
        assertEquals(
            expected = DayOfWeek.THURSDAY,
            actual = "четверг".toDayOfWeekFullNameOrNull()
        )
        assertEquals(
            expected = DayOfWeek.FRIDAY,
            actual = "пятница".toDayOfWeekFullNameOrNull()
        )
        assertEquals(
            expected = DayOfWeek.SATURDAY,
            actual = "суббота".toDayOfWeekFullNameOrNull()
        )
        assertEquals(
            expected = DayOfWeek.SUNDAY,
            actual = "Воскресенье".toDayOfWeekFullNameOrNull()
        )
        assertEquals(
            expected = null,
            actual = "ЛоПатА".toDayOfWeekFullNameOrNull()
        )
        assertEquals(
            expected = null,
            actual = "суб".toDayOfWeekFullNameOrNull()
        )
        assertEquals(
            expected = DayOfWeek.MONDAY,
            actual = "ПонЕдеЛьниК".toDayOfWeekFullNameOrNull()
        )
    }
}
