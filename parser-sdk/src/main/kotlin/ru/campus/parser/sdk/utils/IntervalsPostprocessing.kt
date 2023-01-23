/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import ru.campus.parser.sdk.model.Schedule

fun List<Schedule.Interval>.groupLessonsInIntervals(): List<Schedule.Interval> {
    return groupBy { Triple(it.number, it.start, it.end) }.map { (group, intervals) ->
        val lessons: List<Schedule.Lesson> = intervals.flatMap { it.lessons }.distinct()

        Schedule.Interval(
            number = group.first,
            start = group.second,
            end = group.third,
            lessons = lessons
        )
    }
}
