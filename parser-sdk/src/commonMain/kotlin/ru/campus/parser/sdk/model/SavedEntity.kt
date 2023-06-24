/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

data class SavedEntity(
    val entity: Entity,
    val id: String,
    val status: EntityStatus,
)
