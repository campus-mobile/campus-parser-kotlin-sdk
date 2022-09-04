/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

private object SystemOutputHandler : Handler() {
    init {
        formatter = SimpleFormatter()
    }

    override fun publish(record: LogRecord) {
        val formattedMessage = formatter.format(record)
        if (record.level === Level.WARNING) {
            System.err.println(formattedMessage)
        } else {
            println(formattedMessage)
        }
    }

    override fun flush() = Unit

    @Throws(SecurityException::class)
    override fun close() = Unit
}

fun createSystemOutputLogger(name: String): Logger {
    return Logger.getLogger(name).apply {
        useParentHandlers = false
        addHandler(SystemOutputHandler)
    }
}
