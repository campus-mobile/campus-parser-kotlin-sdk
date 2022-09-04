/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EntityStatus {
    @SerialName("new")
    New,

    @SerialName("same")
    Same,

    @SerialName("updated")
    Updated
}
