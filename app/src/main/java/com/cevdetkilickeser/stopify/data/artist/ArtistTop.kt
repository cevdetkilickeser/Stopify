package com.cevdetkilickeser.stopify.data.artist


import com.google.gson.annotations.SerializedName

data class ArtistTop(
    @SerializedName("data")
    val artistTopTracksDataList: List<ArtistTopTracks>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("next")
    val next: String
)