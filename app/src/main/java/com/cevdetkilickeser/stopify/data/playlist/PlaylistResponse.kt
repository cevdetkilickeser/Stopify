package com.cevdetkilickeser.stopify.data.playlist


import com.google.gson.annotations.SerializedName

data class PlaylistResponse(
    @SerializedName("data")
    val trackList: List<Track>
)