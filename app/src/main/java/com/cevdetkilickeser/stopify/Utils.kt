package com.cevdetkilickeser.stopify

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun String.urlToString(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.name())
}