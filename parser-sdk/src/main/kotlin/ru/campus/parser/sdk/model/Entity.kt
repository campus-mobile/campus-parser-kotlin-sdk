/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

import kotlinx.serialization.Serializable
import ru.campus.parser.sdk.utils.assertLength
import ru.campus.parser.sdk.utils.assertMin
import ru.campus.parser.sdk.utils.assertUrl

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
        assertLength(::name, minLength = 2, maxLength = 120)
        assertLength(::newName, minLength = 2, maxLength = 120)
        assertLength(::code, minLength = 1, maxLength = 120)
        assertLength(::newCode, minLength = 1, maxLength = 120)
        assertUrl(::scheduleUrl)
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
            assertLength(::faculty, length = 1)
            assertLength(::specialty, length = 1)
            assertMin(::course, minValue = 0)
            assertLength(::degree, length = 1)
            assertLength(::educationForm, length = 1)
            assertLength(::department, length = 1)
        }
    }
}
