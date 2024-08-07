package com.cevdetkilickeser.stopify.data.model.playlist


import com.google.gson.annotations.SerializedName

data class PlaylistResponse(
    @SerializedName("data")
    val trackList: List<Track>
)