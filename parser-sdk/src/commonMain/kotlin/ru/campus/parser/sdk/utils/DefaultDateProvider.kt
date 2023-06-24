/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.campus.parser.sdk.DateProvider

fun createDefaultDateProvider(): DateProvider {
    return DateProvider {
        Clock.System.now().toLocalDateTime(TimeZone.of("Asia/Novosibirsk"))
    }
}
