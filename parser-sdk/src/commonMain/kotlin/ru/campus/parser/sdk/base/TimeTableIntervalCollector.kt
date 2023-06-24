/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.base

import ru.campus.parser.sdk.model.TimeTableInterval

interface TimeTableIntervalCollector {
    suspend fun collectIntervals(): List<TimeTableInterval>
}
