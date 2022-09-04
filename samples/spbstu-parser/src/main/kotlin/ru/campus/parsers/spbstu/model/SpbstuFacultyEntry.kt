/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpbstuFacultyEntry(
    @SerialName("id") val facultyCode: Int,
    @SerialName("name") val facultyName: String,
    @SerialName("abbr") val abbreviation: String,
)
