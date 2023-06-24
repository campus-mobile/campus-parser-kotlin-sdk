/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResult(
    val new: Int,
    val updated: Int,
)
