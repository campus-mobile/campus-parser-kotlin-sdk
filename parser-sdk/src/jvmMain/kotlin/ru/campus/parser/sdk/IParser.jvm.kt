/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk

import kotlinx.coroutines.runBlocking
import ru.campus.parser.sdk.model.ParserResult

fun IParser.parseBlocking(): ParserResult {
    return runBlocking { parse() }
}
