package com.cevdetkilickeser.stopify.data.search

import com.cevdetkilickeser.stopify.data.playlist.Track
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("data")
    val trackList: List<Track>
)