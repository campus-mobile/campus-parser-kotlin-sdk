/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.logging

interface Logger {
    fun error(message: String, exc: Throwable?, vararg args: Any)
    fun warn(message: String, exc: Throwable?, vararg args: Any)
    fun info(message: String, vararg args: Any)

    fun warn(message: String) = warn(message, exc = null)
    fun error(message: String) = error(message, exc = null)
}
