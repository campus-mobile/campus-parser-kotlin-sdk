/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import kotlin.reflect.KProperty

actual fun <T : String?> assertTimeValid(property: KProperty<T>) {
    // will be rework later
}

actual fun <T : String?> assertLength(property: KProperty<T>, minLength: Int, maxLength: Int) {
    // will be rework later
}

actual fun <T : String?> assertUrl(property: KProperty<T>) {
    // will be rework later
}

actual fun <T : String?> assertLength(property: KProperty<T>, length: Int) {
    // will be rework later
}

actual fun <T : Int?> assertMin(property: KProperty<T>, minValue: Int) {
    // will be rework later
}
