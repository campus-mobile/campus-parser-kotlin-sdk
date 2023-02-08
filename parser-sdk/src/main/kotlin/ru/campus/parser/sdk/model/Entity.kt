/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

import kotlinx.serialization.Serializable
import ru.campus.parser.sdk.utils.assertEntityCode
import ru.campus.parser.sdk.utils.assertEntityName
import ru.campus.parser.sdk.utils.assertValidUrl

@Serializable
data class Entity(
    val type: Type,
    val name: String,
    val newName: String? = null,
    val code: String? = null,
    val newCode: String? = null,
    val scheduleUrl: String? = null,
    val extra: Extra? = null,
) {
    init {
        assertEntityName(name)
        newName?.also { assertEntityName(it) }
        code?.also { assertEntityCode(it) }
        newCode?.also { assertEntityCode(it) }
        scheduleUrl?.also { assertValidUrl(it) }
    }

    enum class Type {
        Group,
        Teacher
    }

    @Serializable
    data class Extra(
        val faculty: String? = null,
        val specialty: String? = null,
        val course: Int? = null,
        val degree: String? = null,
        val educationForm: String? = null,
        val department: String? = null,
    ) {
        init {
            faculty?.also { assert(it.length >= 2) { "faculty length should be >= 2" } }
            specialty?.also { assert(it.length >= 2) { "specialty length should be >= 2" } }
            course?.also { assert(it >= 1) { "course should be >= 1" } }
            degree?.also { assert(it.length >= 2) { "degree length should be >= 2" } }
            educationForm?.also { assert(it.length >= 2) { "educationForm length should be >= 2" } }
            department?.also { assert(it.length >= 2) { "department length should be >= 2" } }
        }
    }
}
