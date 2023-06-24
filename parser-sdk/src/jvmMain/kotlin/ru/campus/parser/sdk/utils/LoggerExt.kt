/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.message.MessageFormatMessage
import ru.campus.parser.sdk.logging.Logger as CampusLogger

fun Logger.asCampusLogger(): CampusLogger {
    val l4jLogger: Logger = this
    return object : CampusLogger {
        override fun error(message: String, exc: Throwable?, vararg args: Any) {
            val formatting = MessageFormatMessage(message, args)
            l4jLogger.error(formatting.formattedMessage, exc)
        }

        override fun warn(message: String, exc: Throwable?, vararg args: Any) {
            val formatting = MessageFormatMessage(message, args)
            l4jLogger.warn(formatting.formattedMessage, exc)
        }

        override fun info(message: String, vararg args: Any) {
            l4jLogger.info(message, *args)
        }
    }
}
