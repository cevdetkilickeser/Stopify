package com.cevdetkilickeser.stopify.data.model.search

import com.cevdetkilickeser.stopify.data.model.playlist.Track
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("data")
    val trackList: List<Track>
)