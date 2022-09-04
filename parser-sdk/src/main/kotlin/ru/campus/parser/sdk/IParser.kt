/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk

import kotlinx.coroutines.runBlocking
import ru.campus.parser.sdk.model.ParserResult
import java.util.logging.Logger

interface IParser {
    /**
     * Флаг означающий что парсер должен выгрузить только список сущностей, без расписания.
     * Применяется для выгрузки списка преподавателей для отзывов.
     */
    val isWithoutSchedule: Boolean

    /**
     * Флаг для парсеров, где сущностей меньше 10
     * */
    val isLessTenEntities: Boolean

    /**
     * Логгер конкретного парсера.
     */
    val logger: Logger

    suspend fun parse(): ParserResult
}

fun IParser.parseBlocking(): ParserResult {
    return runBlocking { parse() }
}
