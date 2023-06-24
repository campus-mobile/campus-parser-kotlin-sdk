/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.model

fun Credentials.Companion.get(parserName: String): Credentials {
    val nameUpper: String = parserName.uppercase()
    return Credentials(
        username = System.getenv("${nameUpper}_USERNAME"),
        password = System.getenv("${nameUpper}_PASSWORD")
    )
}
