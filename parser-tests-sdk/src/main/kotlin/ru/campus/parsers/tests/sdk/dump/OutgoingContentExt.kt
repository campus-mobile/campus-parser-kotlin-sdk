/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parsers.tests.sdk.dump

import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.content.OutgoingContent

internal suspend fun OutgoingContent.hash(): String {
    return when (this) {
        is OutgoingContent.NoContent -> "NoContent"
        is OutgoingContent.ReadChannelContent -> this.toByteArray().let { Utils.md5(it) }
        is OutgoingContent.WriteChannelContent -> throw IllegalArgumentException("can't hash WriteChannelContent")
        is OutgoingContent.ByteArrayContent -> this.bytes().let { Utils.md5(it) }
        is OutgoingContent.ProtocolUpgrade -> throw IllegalArgumentException("can't hash ProtocolUpgrade")
    }
}
