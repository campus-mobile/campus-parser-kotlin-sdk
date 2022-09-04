/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk

import ru.campus.parser.sdk.model.Entity

open class ScheduleNotComposedException(
    val entity: Entity,
    message: String = "Schedule of ${entity.name} not composed yet",
) : RuntimeException(message)

class TeacherNotEmployedException(
    teacher: Entity,
) : ScheduleNotComposedException(entity = teacher, message = "${teacher.name} not employed")
