/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

import kotlinx.serialization.Serializable

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
    )
}
