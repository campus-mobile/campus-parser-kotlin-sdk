/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk

interface Logger {
    fun error(message: String, exc: Throwable?, vararg args: Any)
    fun warn(message: String, exc: Throwable?, vararg args: Any)
    fun info(message: String, vararg args: Any)
}
