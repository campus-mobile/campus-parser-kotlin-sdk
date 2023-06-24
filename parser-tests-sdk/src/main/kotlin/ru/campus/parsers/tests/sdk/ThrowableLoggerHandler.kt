/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.tests.sdk

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.Marker
import org.apache.logging.log4j.message.Message
import ru.campus.parser.sdk.utils.asCampusLogger

object ThrowableLogger {
    operator fun invoke(): ru.campus.parser.sdk.logging.Logger {
        val logger: Logger = LogManager.getLogger(ThrowableLogger::class.java)
        return object : Logger by logger {
            override fun logMessage(
                level: org.apache.logging.log4j.Level?,
                marker: Marker?,
                fqcn: String?,
                location: StackTraceElement?,
                message: Message?,
                throwable: Throwable?
            ) {
                super.logMessage(level, marker, fqcn, location, message, throwable)

                if (throwable != null) {
                    throw throwable
                }
                if (level in listOf(org.apache.logging.log4j.Level.ERROR, org.apache.logging.log4j.Level.FATAL)) {
                    throw IllegalStateException(message?.formattedMessage ?: "unknown")
                }
            }
        }.asCampusLogger()
    }
}
