/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.model

data class Credentials(
    val username: String,
    val password: String,
) {
    companion object {
        fun get(parserName: String): Credentials {
            val nameUpper = parserName.uppercase()
            return Credentials(
                username = System.getenv("${nameUpper}_USERNAME"),
                password = System.getenv("${nameUpper}_PASSWORD")
            )
        }
    }
}
