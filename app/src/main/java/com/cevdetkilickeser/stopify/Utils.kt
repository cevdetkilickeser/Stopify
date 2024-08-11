package com.cevdetkilickeser.stopify

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun String.convertStandardCharsets(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.name())
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}