/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

import kotlinx.datetime.DayOfWeek

data class WeekScheduleItem(
    val dayOfWeek: DayOfWeek,
    val timeTableInterval: TimeTableInterval,
    val dayCondition: DatePredicate,
    val lesson: Schedule.Lesson,
) {
    fun isSameTime(other: WeekScheduleItem): Boolean {
        return isSameTimeWithoutCondition(other) && dayCondition == other.dayCondition
    }

    fun isSameTimeWithoutCondition(other: WeekScheduleItem): Boolean {
        return dayOfWeek == other.dayOfWeek &&
                timeTableInterval.lessonNumber == other.timeTableInterval.lessonNumber
    }
}
