package com.cevdetkilickeser.stopify.data.model.album


import com.google.gson.annotations.SerializedName

data class TrackDataArtist(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("tracklist")
    val trackList: String,
    @SerializedName("type")
    val type: String
)