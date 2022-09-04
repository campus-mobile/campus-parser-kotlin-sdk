/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntityResult(
    @SerialName("_id")
    val id: String,
    val code: String,
    val name: String,
    val status: EntityStatus,
)
