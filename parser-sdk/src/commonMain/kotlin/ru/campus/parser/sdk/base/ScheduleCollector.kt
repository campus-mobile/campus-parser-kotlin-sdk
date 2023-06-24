/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.base

import ru.campus.parser.sdk.model.Entity
import ru.campus.parser.sdk.model.TimeTableInterval
import ru.campus.parser.sdk.model.WeekScheduleItem

interface ScheduleCollector {
    suspend fun collectSchedule(entity: Entity, intervals: List<TimeTableInterval>): Result

    data class Result(
        val processedEntity: Entity,
        val weekScheduleItems: List<WeekScheduleItem>
    )
}
