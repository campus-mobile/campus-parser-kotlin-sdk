/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.logging

import org.apache.logging.log4j.LogManager
import ru.campus.parser.sdk.utils.asCampusLogger
import kotlin.reflect.KClass

actual fun createLogger(kClass: KClass<*>): Logger {
    return LogManager.getLogger(kClass.java).asCampusLogger()
}
