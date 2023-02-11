/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import java.net.URI

private val timeRegex = Regex("""\d\d:\d\d""")

fun assertTimeValid(time: String) {
    assert(timeRegex.matches(time)) { "invalid format of time in $time" }
}

fun assertEntityName(name: String) {
    assert(name.length in 2..119) { "name should be more 1 and less 120 characters" }
}

fun assertEntityCode(code: String) {
    assert(code.length > 1) { "code should be more than 1 character" }
}

fun assertValidUrl(url: String) {
    assert(URI.create(url) != null) { "url should be valid" }
}
