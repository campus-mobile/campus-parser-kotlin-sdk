/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

data class SavedSchedule(
    val savedEntity: SavedEntity,
    val schedule: List<Schedule>,
    val addedCount: Int,
    val updatedCount: Int,
)
