/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.tests.sdk

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

fun String.singleLineJson(): String {
    return Json.decodeFromString<JsonElement>(this).toString()
}
