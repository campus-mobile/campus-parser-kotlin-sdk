/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.Serializable

@Serializable
data class SpbstuFaculty(
    val groups: List<SpbstuGroup>,
    val faculty: SpbstuFacultyEntry,
)
