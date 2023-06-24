/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.logging

import kotlin.reflect.KClass

expect fun createLogger(kClass: KClass<*>): Logger
