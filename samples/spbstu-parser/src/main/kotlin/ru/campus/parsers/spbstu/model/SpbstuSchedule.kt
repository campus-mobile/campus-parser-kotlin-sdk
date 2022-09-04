/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.Serializable

@Serializable
data class SpbstuSchedule(
    val week: SpbstuWeekInfo,
    val days: List<SpbstuDay>
)
