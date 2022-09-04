/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

data class TimeTableInterval(
    val lessonNumber: Int,
    val startTime: String,
    val endTime: String
)
