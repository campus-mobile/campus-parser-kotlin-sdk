/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import kotlin.reflect.KProperty

expect fun <T : String?> assertTimeValid(property: KProperty<T>)

expect fun <T : String?> assertLength(property: KProperty<T>, minLength: Int, maxLength: Int)

expect fun <T : String?> assertUrl(property: KProperty<T>)

expect fun <T : String?> assertLength(property: KProperty<T>, length: Int)

expect fun <T : Int?> assertMin(property: KProperty<T>, minValue: Int)
