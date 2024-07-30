package com.cevdetkilickeser.stopify.data.search


import com.google.gson.annotations.SerializedName

data class SearchByArtistResponse(
    @SerializedName("data")
    val artistDataList: List<ArtistData>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("next")
    val next: String
)