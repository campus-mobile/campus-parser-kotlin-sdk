/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import java.math.BigInteger
import java.security.MessageDigest

fun String.md5(): String {
    val md: MessageDigest = MessageDigest.getInstance("MD5")
    return BigInteger(
        1,
        md.digest(this.toByteArray())
    ).toString(16).padStart(32, '0')
}
