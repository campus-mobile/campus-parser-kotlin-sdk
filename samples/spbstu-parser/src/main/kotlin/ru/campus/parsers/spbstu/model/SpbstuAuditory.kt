/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.Serializable

@Serializable
data class SpbstuAuditory(
    val id: Int,
    val name: String,
    val building: SpbstuBuilding,
)
