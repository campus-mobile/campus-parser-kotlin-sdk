/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpbstuLessonParam(
    @SerialName("id") val paramId: Int,
    @SerialName("name") val typeName: String,
    val abbr: String?
)
