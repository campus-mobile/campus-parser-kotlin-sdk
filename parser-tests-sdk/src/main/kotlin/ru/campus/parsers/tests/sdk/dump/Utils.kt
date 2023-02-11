/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parsers.tests.sdk.dump

import java.math.BigInteger
import java.security.MessageDigest

internal object Utils {
    private val md = MessageDigest.getInstance("MD5")

    fun md5(input: ByteArray): String {
        return BigInteger(1, md.digest(input)).toString(16).padStart(32, '0')
    }
}
