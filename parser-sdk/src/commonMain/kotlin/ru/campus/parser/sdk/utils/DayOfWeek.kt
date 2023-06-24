/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import kotlinx.datetime.DayOfWeek

fun String.toDayOfWeek(): DayOfWeek {
    return when (this.lowercase()) {
        "пн" -> DayOfWeek.MONDAY
        "вт" -> DayOfWeek.TUESDAY
        "ср" -> DayOfWeek.WEDNESDAY
        "чт" -> DayOfWeek.THURSDAY
        "пт" -> DayOfWeek.FRIDAY
        "сб" -> DayOfWeek.SATURDAY
        "вс" -> DayOfWeek.SUNDAY
        else -> throw IllegalArgumentException("invalid day of week $this")
    }
}

fun String.toDayOfWeekFullNameOrNull(): DayOfWeek? {
    return when (this.lowercase()) {
        "понедельник" -> DayOfWeek.MONDAY
        "вторник" -> DayOfWeek.TUESDAY
        "среда" -> DayOfWeek.WEDNESDAY
        "четверг" -> DayOfWeek.THURSDAY
        "пятница" -> DayOfWeek.FRIDAY
        "суббота" -> DayOfWeek.SATURDAY
        "воскресенье" -> DayOfWeek.SUNDAY
        else -> null
    }
}
