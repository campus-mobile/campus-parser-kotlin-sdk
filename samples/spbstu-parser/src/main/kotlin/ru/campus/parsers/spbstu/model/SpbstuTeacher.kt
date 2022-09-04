/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpbstuTeacher(
    @SerialName("id") val code: Int,
    @SerialName("full_name") val name: String,
)
