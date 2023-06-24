/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

val Int.weekName: String get() = if (this % 2 == 0) "Четная" else "Нечетная"
