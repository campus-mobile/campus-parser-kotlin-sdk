/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.Serializable

@Serializable
data class SpbstuBuilding(
    val id: Int,
    val name: String,
    val address: String
)
