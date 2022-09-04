/*
 * Copyright 2022 LLC Campus.
 */

package ru.campus.parser.sdk.api

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class InMemoryCookieJar : CookieJar {
    private var cookies: List<Cookie> = emptyList()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies = this.cookies + cookies
    }
}
