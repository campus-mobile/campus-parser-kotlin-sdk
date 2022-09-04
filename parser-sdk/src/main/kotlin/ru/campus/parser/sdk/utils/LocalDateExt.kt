/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import java.time.DayOfWeek
import kotlin.math.floor

/**
 * Номер недели, начиная с 1 понедельника (от начала года)
 */
val LocalDate.weekNumber: Int
    get() {
        val firstDayOfYear = LocalDate(year = year, monthNumber = 1, dayOfMonth = 1)
        val firstDayOfWeek: DayOfWeek = firstDayOfYear.dayOfWeek
        return if (firstDayOfWeek == DayOfWeek.MONDAY) this.dayOfYear.weeksFromDays
        else if (dayOfYear < (DayOfWeek.SUNDAY.value - firstDayOfWeek.value) + 2) {
            LocalDate(year = year - 1, monthNumber = 12, dayOfMonth = 31).weekNumber
        } else {
            val daysFromFirstMonday = this.dayOfYear - (DayOfWeek.SUNDAY.value + 1 - firstDayOfWeek.value)
            (daysFromFirstMonday - 1).weeksFromDays
        }
    }

val Int.weeksFromDays: Int get() = floor(this / 7.0).toInt() + 1

fun LocalDate.weeksUntil(other: LocalDate): Int {
    val daysFromStartOfStudy: Int = this.daysUntil(other)
    return daysFromStartOfStudy.weeksFromDays
}
