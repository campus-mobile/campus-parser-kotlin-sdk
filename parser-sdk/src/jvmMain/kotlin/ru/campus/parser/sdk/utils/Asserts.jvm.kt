/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import org.apache.commons.validator.routines.UrlValidator
import org.apache.commons.validator.routines.UrlValidator.ALLOW_2_SLASHES
import kotlin.reflect.KProperty

private val timeRegex = Regex("""\d\d:\d\d""")
private val urlValidator = UrlValidator(ALLOW_2_SLASHES)

actual fun <T : String?> assertTimeValid(property: KProperty<T>) {
    val value: String = property.call() ?: return

    assert(timeRegex.matches(value)) { "${property.name} [$value] have invalid format" }
    val (hours: Int, minutes: Int) = value.split(":").map { it.toInt() }
    assert(hours in 0..23) { "hours should be in 0..23" }
    assert(minutes in 0..59) { "minutes should be in 0..59" }
}

actual fun <T : String?> assertLength(property: KProperty<T>, minLength: Int, maxLength: Int) {
    val value: String = property.call() ?: return
    assert(value.length in minLength until maxLength) { "${property.name} [$value] should be more than $minLength and less $maxLength characters" }
}

actual fun <T : String?> assertUrl(property: KProperty<T>) {
    val value: String = property.call() ?: return
    assert(urlValidator.isValid(value)) { "${property.name} [$value] invalid url" }
}

actual fun <T : String?> assertLength(property: KProperty<T>, length: Int) {
    val value: String = property.call() ?: return
    assert(value.length > length) { "${property.name} [$value] should be more than $length character" }
}

actual fun <T : Int?> assertMin(property: KProperty<T>, minValue: Int) {
    val value: Int = property.call() ?: return
    assert(value > minValue) { "${property.name} [$value] should be more than $minValue" }
}
