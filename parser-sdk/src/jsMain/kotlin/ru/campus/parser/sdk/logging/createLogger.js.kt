/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.logging

import kotlin.reflect.KClass

actual fun createLogger(kClass: KClass<*>): Logger {
    val prefix: String = "[" + (kClass.simpleName ?: "?") + "]"
    return object : Logger {
        override fun error(message: String, exc: Throwable?, vararg args: Any) {
            console.error("$prefix $exc $message", args)
            exc?.printStackTrace()
        }

        override fun warn(message: String, exc: Throwable?, vararg args: Any) {
            console.warn("$prefix $exc $message", args)
            exc?.printStackTrace()
        }

        override fun info(message: String, vararg args: Any) {
            console.info("$prefix $message", args)
        }
    }
}
