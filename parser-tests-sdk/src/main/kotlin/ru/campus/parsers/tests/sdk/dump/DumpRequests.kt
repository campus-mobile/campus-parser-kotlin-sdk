/*
 * Copyright 2023 LLC Campus.
 */

package ru.campus.parsers.tests.sdk.dump

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.encodeURLParameter
import io.ktor.util.AttributeKey
import java.io.File

internal class DumpRequests(
    private val dirName: String
) {
    class Config {
        lateinit var dirName: String
    }

    companion object Feature : HttpClientPlugin<Config, DumpRequests> {
        override val key: AttributeKey<DumpRequests> = AttributeKey("DumpRequests")

        override fun prepare(block: Config.() -> Unit): DumpRequests {
            val config = Config()
            config.block()
            return DumpRequests(config.dirName)
        }

        override fun install(feature: DumpRequests, scope: HttpClient) {
            scope.responsePipeline.intercept(HttpResponsePipeline.Parse) { container ->
                val request: HttpRequest = this.context.request
                val url: String = request.url.toString().encodeURLParameter()
                val method: String = request.method.value
                val bodyHash: String = request.content.hash()

                val fileName = "$method-$url-$bodyHash.resp"

                val dir = File(feature.dirName)
                dir.mkdirs()
                val file = File(dir, fileName)
                file.writeText(container.response.toString())
            }
        }

    }
}
