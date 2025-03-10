/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import org.apache.commons.validator.routines.UrlValidator
import org.apache.commons.validator.routines.UrlValidator.ALLOW_2_SLASHES
import kotlin.reflect.KProperty

private val timeRegex = Regex("""\d\d:\d\d""")
private val urlValidator = UrlValidator(ALLOW_2_SLASHES)

fun <T : String?> assertTimeValid(property: KProperty<T>) {
    val value: String = property.call() ?: return

    sdkAssert(timeRegex.matches(value)) { "${property.name} [$value] have invalid format" }
    val (hours: Int, minutes: Int) = value.split(":").map { it.toInt() }
    sdkAssert(hours in 0..23) { "hours should be in 0..23" }
    sdkAssert(minutes in 0..59) { "minutes should be in 0..59" }
}

fun <T : String?> assertLength(property: KProperty<T>, minLength: Int, maxLength: Int) {
    val value: String = property.call() ?: return
    sdkAssert(value.length in minLength until maxLength) { "${property.name} [$value] should be more than $minLength and less $maxLength characters" }
}

fun <T : String?> assertUrl(property: KProperty<T>) {
    val value: String = property.call() ?: return
    sdkAssert(urlValidator.isValid(value)) { "${property.name} [$value] invalid url" }
}

fun <T : String?> assertLength(property: KProperty<T>, length: Int) {
    val value: String = property.call() ?: return
    sdkAssert(value.length > length) { "${property.name} [$value] should be more than $length character" }
}

fun <T : Int?> assertMin(property: KProperty<T>, minValue: Int) {
    val value: Int = property.call() ?: return
    sdkAssert(value > minValue) { "${property.name} [$value] should be more than $minValue" }
}

fun sdkAssert(value: Boolean, lazyMessage: () -> Any) {
    if (!value) {
        val message = lazyMessage()
        val error = AssertionError(message)
        if (System.getenv("ENVIRONMENT") == "production") {
            error.printStackTrace()
        } else {
            throw error
        }
    }
}