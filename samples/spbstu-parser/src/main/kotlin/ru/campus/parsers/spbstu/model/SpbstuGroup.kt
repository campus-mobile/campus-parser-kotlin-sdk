/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpbstuGroup(
    @SerialName("id") val code: Int,
    @SerialName("name") val groupName: String,
    @SerialName("level") val course: Int,
    @SerialName("type") val educationForm: String,
    @SerialName("kind") val degree: Int,
    @SerialName("spec") val specialization: String?,
    @SerialName("year") val studyYear: Int?,
)
