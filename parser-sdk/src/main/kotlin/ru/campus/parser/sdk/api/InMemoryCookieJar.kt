/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.api

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.concurrent.atomic.AtomicReference

// do same as io.ktor.client.features.cookies.AcceptAllCookiesStorage
class InMemoryCookieJar : CookieJar {
    private val container: AtomicReference<List<Cookie>> = AtomicReference(emptyList())

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return container.get().filter { it.matches(url) }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val validCookies: List<Cookie> = cookies.filter { it.name.isNotBlank() }

        do {
            val currentList: List<Cookie> = container.get()

            val newList: List<Cookie> = currentList.filter { cookie ->
                cookie.matches(url) && cookie.name !in validCookies.map { it.name }
            } + validCookies
        } while (container.compareAndSet(currentList, newList).not())
    }
}
