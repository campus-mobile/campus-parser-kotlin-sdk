/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

data class ParserResult(
    val entitiesCount: Int,
    val entitiesWithLesson: Int,
    val errorsCount: Int,
    val scheduleAddedCount: Int,
    val scheduleUpdatedCount: Int,
    val savedEntities: List<SavedEntity>,
    val savedSchedules: List<SavedSchedule>,
) {
    val entitiesWithoutLesson: Int get() = entitiesCount - entitiesWithLesson
}
