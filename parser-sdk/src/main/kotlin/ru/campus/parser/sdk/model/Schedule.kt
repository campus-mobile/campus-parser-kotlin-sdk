/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

import kotlinx.serialization.Serializable
import org.apache.commons.validator.routines.UrlValidator
import ru.campus.parser.sdk.utils.assertTimeValid
import ru.campus.parser.sdk.utils.assertValidUrl
import java.net.URI

@Serializable
data class Schedule(
    val date: String,
    val week: String? = null,
    val intervals: List<Interval> = emptyList(),
) {
    @Serializable
    data class Interval(
        val number: Int? = null,
        val start: String,
        val end: String,
        val lessons: List<Lesson>,
    ) {
        init {
            assertTimeValid(start)
            assertTimeValid(end)
        }
    }

    @Serializable
    data class Lesson(
        val subject: String,
        val comment: String? = null,
        val type: String? = null,
        val classroom: String? = null,
        val building: Building? = null,
        val teachers: List<Entity> = emptyList(),
        val groups: List<Entity> = emptyList(),
        val subgroups: List<String> = emptyList(),
        val links: List<Link> = emptyList(),
    ) {
        init {
            assert(subject.length >= 2) { "subject should NOT be shorter than 2 characters" }
        }
    }

    @Serializable
    data class Entity(
        val name: String,
        val code: String? = null,
    )

    @Serializable
    data class Link(
        val title: String,
        val url: String,
    ) {
        init {
            assert(UrlValidator().isValid(url)) { "not valid url" }
        }
    }

    @Serializable
    data class Building(
        val name: String?,
        val address: String?,
        val coordinate: Coordinate?,
    )
}

fun List<Schedule.Entity>.enrichEntities(
    fullEntities: List<Entity>,
    onNotFound: (Schedule.Entity) -> Unit,
): List<Schedule.Entity> {
    return map { shortEntity ->
        val fullEntityByCode = fullEntities.firstOrNull { it.code == shortEntity.code }
        if (fullEntityByCode != null) {
            return@map Schedule.Entity(
                name = fullEntityByCode.name,
                code = shortEntity.code
            )
        }

        val fullEntityByName = fullEntities.singleOrNull { it.name == shortEntity.name }
        if (fullEntityByName != null) {
            return@map Schedule.Entity(
                name = shortEntity.name,
                code = fullEntityByName.code
            )
        }

        onNotFound(shortEntity)
        return@map shortEntity
    }
}
