/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.api

import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.OkHttpClient
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
    return proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("x.botproxy.net", 8080)))
}

fun OkHttpClient.Builder.setupAuthenticator(): OkHttpClient.Builder {
    val username: String = System.getenv("PROXY_USERNAME")
    val password: String = System.getenv("PROXY_PASSWORD")
    val credential = Credentials.basic(username, password)

    val proxyAuthenticator: Authenticator = object : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request {
            return response.request.newBuilder()
                .header("Proxy-Authorization", credential)
                .build()
        }
    }
    return proxyAuthenticator(proxyAuthenticator)
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
