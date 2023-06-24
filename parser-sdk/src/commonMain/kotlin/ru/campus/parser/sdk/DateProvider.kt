/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk

import kotlinx.datetime.LocalDateTime

fun interface DateProvider {
    fun getCurrentDateTime(): LocalDateTime
}
