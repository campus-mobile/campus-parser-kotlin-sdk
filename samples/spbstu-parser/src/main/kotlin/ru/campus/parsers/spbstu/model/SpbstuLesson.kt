/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.spbstu.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpbstuLesson(
    val subject: String,
    val type: Int,
    @SerialName("additional_info") val comment: String?,
    @SerialName("time_start") val intervalStart: String,
    @SerialName("time_end") val intervalEnd: String,
    @SerialName("typeObj") val lessonParams: SpbstuLessonParam,
    val teachers: List<SpbstuTeacher>?,
    @SerialName("auditories") val auditory: List<SpbstuAuditory>?,
    @SerialName("webinar_url") val webinarLink: String?,
    @SerialName("lms_url") val lmsLink: String?,
)
