/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpbstuWeekInfo(
    @SerialName("date_start") val startDate: String,
    @SerialName("date_end") val endDate: String,
    @SerialName("is_odd") val isOdd: Boolean
)
