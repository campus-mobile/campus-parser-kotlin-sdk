/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.header
import io.ktor.client.request.prepareGet
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.FileNotFoundException

suspend fun getFileFromUrl(
    httpClient: HttpClient,
    urlString: String,
    cacheDirectoryName: String,
    fileExtension: String,
    logger: Logger,
): File {
    val fileName: String = urlString.md5()
    val cacheDir = File("cache/$cacheDirectoryName")
    cacheDir.mkdirs()
    val file = File(cacheDir, "$fileName.$fileExtension")
    val etagFile = File(cacheDir, "$fileName.etag")
    val eTagHeader: String? = try {
        etagFile.readText()
    } catch (_: FileNotFoundException) {
        null
    }
    return httpClient.prepareGet(urlString) {
        eTagHeader?.let { header("If-None-Match", it) }
        expectSuccess = false
    }.execute { httpResponse ->
        if (httpResponse.status == HttpStatusCode.NotModified) {
            return@execute file
        }
        if (httpResponse.status == HttpStatusCode.NotFound) {
            throw FileNotFoundException("file at $urlString not found")
        }

        val etagValue: String = httpResponse.headers["ETag"]
            ?: throw IllegalStateException("no ETag in response")

        withContext(Dispatchers.IO) {
            if (!etagFile.exists()) {
                etagFile.createNewFile()
            }
            etagFile.writeText(etagValue)
        }

        val channel: ByteReadChannel = httpResponse.body()

        if (channel.isClosedForRead.not()) {
            withContext(Dispatchers.IO) {
                file.delete()
                file.createNewFile()
            }

            while (!channel.isClosedForRead) {
                val packet: ByteReadPacket = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                while (!packet.isEmpty) {
                    val bytes: ByteArray = packet.readBytes()
                    withContext(Dispatchers.IO) { file.appendBytes(bytes) }
                }
            }

            logger.info("A file saved to {}", file.path)
        }

        file
    }
}

suspend fun simpleFileFromUrl(
    httpClient: HttpClient,
    urlString: String,
    cacheDirectoryName: String,
    fileExtension: String,
    logger: Logger,
): File {
    val fileName: String = urlString.md5()
    val cacheDir = File("cache/$cacheDirectoryName")
    cacheDir.mkdirs()
    val file = File(cacheDir, "$fileName.$fileExtension")
    return httpClient.prepareGet(urlString).execute { httpResponse ->
        if (httpResponse.status == HttpStatusCode.NotFound) {
            throw FileNotFoundException("file at $urlString not found")
        }
        val channel: ByteReadChannel = httpResponse.body()

        if (!channel.isClosedForRead) {
            withContext(Dispatchers.IO) {
                file.delete()
                file.createNewFile()
            }

            while (!channel.isClosedForRead) {
                val packet: ByteReadPacket = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                while (!packet.isEmpty) {
                    val bytes: ByteArray = packet.readBytes()
                    withContext(Dispatchers.IO) { file.appendBytes(bytes) }
                }
            }
            logger.info("A file saved to {}", file.path)
        }
        file
    }
}
