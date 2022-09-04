/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.tests.sdk

import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger

class ThrowableLoggerHandler : Handler() {
    override fun publish(record: LogRecord) {
        if (record.thrown != null) {
            throw record.thrown
        }
        if (record.level == Level.WARNING) {
            throw IllegalStateException(record.message)
        }
    }

    override fun flush() = Unit

    override fun close() = Unit
}

@Suppress("FunctionName")
fun ThrowableLogger(): Logger {
    return Logger.getLogger("throwable-test").apply {
        addHandler(ThrowableLoggerHandler())
    }
}
