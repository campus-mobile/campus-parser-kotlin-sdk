/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

// or empty for run tests
fun getParserApiUrl(): String = System.getenv("API_URL").orEmpty()
