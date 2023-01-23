/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parsers.tests.sdk

import java.io.File
import java.nio.charset.Charset

private object ResourcesExt

fun readResourceFile(resourcePath: String, charset: Charset = Charsets.UTF_8): String {
    return ResourcesExt.javaClass.classLoader
        .getResourceAsStream(resourcePath)
        .use { it!!.bufferedReader(charset).readText() }
}

fun readResourceFileBytes(resourcePath: String): ByteArray {
    return ResourcesExt.javaClass.classLoader
        .getResourceAsStream(resourcePath)
        .use { it!!.readBytes() }
}

fun readResourceAsFile(resourcePath: String, charset: Charset = Charsets.UTF_8): File {
    val content: String = readResourceFile(resourcePath, charset)
    return File.createTempFile("resource-", ".tmp").apply {
        writeText(content, charset)
        deleteOnExit()
    }
}
