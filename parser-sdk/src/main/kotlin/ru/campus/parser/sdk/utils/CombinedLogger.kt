/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import java.io.File
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

fun createCombinedLogger(name: String): Logger {
    return createSystemOutputLogger(name).apply {
        val logsDirectory: File = File("logs").apply { mkdirs() }
        val logFile = File(logsDirectory, "$name.log")
        val fileHandler = FileHandler(logFile.absolutePath).apply {
            formatter = SimpleFormatter()
        }
        addHandler(fileHandler)
    }
}
