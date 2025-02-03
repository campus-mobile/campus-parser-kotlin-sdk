/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import kotlin.math.ceil
import kotlin.math.floor
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/**
 * Номер недели, начиная с первой недели, на которую выпало 4 января (международный стандарт).
 */
val LocalDate.weekNumber: Int
    get() {
        // Чтобы понять текущий номер недели, нужно взять расстояние от первого дня первой недели года до сегодняшней и поделить на 7 (вверх)
        // Чтобы понять расстояние от первого дня первой недели года до сегодня, нужно узнать, насколько эта дата отклонена от 1 января.
        // Чтобы это узнать, нужно узнать дату понедельника первой недели года.
        // Дальше просто взять день года (dayOfYear = 1..366) и прибавить к нему недостающие дни до понедельника первой недели.
        // Ну и потом обычным ceil(day/7.0) округлить. Будет 1..7 = 1, 8..14 = 2, ...

        // Достаём первую неделю года (неделю, в которой 4 января)
        val fourthJanuary = LocalDate(year = year, monthNumber = 1, dayOfMonth = 4)
        val dayOfWeekOfFourthJanuary: DayOfWeek = fourthJanuary.dayOfWeek

        // Скок дней надо прибавлять (скок дней недели сожрал прошлый год):
        // ordinal: пн = 0, вс = 6
        // 4 янв = пн => -3 к дате (сожрано этим годом)
        // 4 янв = вс => +3 к дате (сожрано прошлым годом)
        val offset = dayOfWeekOfFourthJanuary.ordinal - 3

        // Считаем номер недели с учетом сдвига
        val weekNumber = ceil((dayOfYear + offset) / 7.0).toInt()
        // Если неделя прошлогодняя, считаем номер ласт недели прошлого года
        if (weekNumber == 0) {
            return LocalDate(year = year - 1, monthNumber = 12, dayOfMonth = 31.coerceAtMost(31 - offset)).weekNumber
        }
        // Если неделя - первая неделя следующего
        if (monthNumber == 12 && dayOfMonth >= 29 && dayOfMonth-dayOfWeek.ordinal >= 29) {
            return 1
        }
        return weekNumber
    }

val Int.weeksFromDays: Int get() = floor(this / 7.0).toInt() + 1

fun LocalDate.weeksUntil(other: LocalDate): Int {
    val daysFromStartOfStudy: Int = this.daysUntil(other)
    return daysFromStartOfStudy.weeksFromDays
}
