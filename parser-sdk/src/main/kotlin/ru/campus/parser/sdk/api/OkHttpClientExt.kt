/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.api

import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.InetSocketAddress
import java.net.Proxy
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

fun OkHttpClient.Builder.setupProxy(): OkHttpClient.Builder {
    val host: String = System.getenv("PROXY_HOST")
    val port: Int = System.getenv("PROXY_PORT").toInt()
    val username: String? = System.getenv("PROXY_USERNAME").takeIf { it.isNotEmpty() }
    val password: String? = System.getenv("PROXY_PASSWORD").takeIf { it.isNotEmpty() }


    val proxy = proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(host, port)))

    return if (username != null && password != null) {
        val credential = Credentials.basic(username, password)

        val proxyAuthenticator: Authenticator = object : Authenticator {
            override fun authenticate(route: Route?, response: Response): Request {
                return response.request.newBuilder()
                    .header("Proxy-Authorization", credential)
                    .build()
            }
        }

        proxy.proxyAuthenticator(proxyAuthenticator)
    } else proxy
}

fun OkHttpClient.Builder.disableSslChecks(): OkHttpClient.Builder {
    // Create a trust manager that does not validate certificate chains
    val allAcceptTrustManager: X509TrustManager = object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return emptyArray()
        }
    }
    // Install the all-trusting trust manager
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, arrayOf(allAcceptTrustManager), SecureRandom())
    // Create an ssl socket factory with our all-trusting manager
    return sslSocketFactory(sslContext.socketFactory, allAcceptTrustManager)
        .hostnameVerifier { _, _ -> true }
}

fun OkHttpClient.Builder.setHttpProtocol(): OkHttpClient.Builder {
    return protocols(listOf(Protocol.HTTP_1_1))
}
