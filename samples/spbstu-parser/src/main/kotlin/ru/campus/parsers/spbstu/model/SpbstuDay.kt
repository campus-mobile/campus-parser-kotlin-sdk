/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpbstuDay(
    @SerialName("weekday") val dayOfWeek: Int,
    val date: String,
    val lessons: List<SpbstuLesson>
)
