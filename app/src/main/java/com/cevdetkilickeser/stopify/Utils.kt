package com.cevdetkilickeser.stopify

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun String.convertStandardCharsets(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.name())
}

fun String.convertStandardCharsetsReplacePlusWithSpace(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.name()).replace("+"," ")
}

fun List<PlayerTrack>.preparePlayerTrackListForRoute(): List<PlayerTrack> {
    return this.map {
        PlayerTrack(
            it.trackId,
            it.trackTitle.convertStandardCharsetsReplacePlusWithSpace(),
            it.trackPreview.convertStandardCharsets(),
            it.trackImage.convertStandardCharsets(),
            it.trackArtistName.convertStandardCharsetsReplacePlusWithSpace()
        )
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    allowSpecialFloatingPointValues = true
    prettyPrint = true
    coerceInputValues = true
}