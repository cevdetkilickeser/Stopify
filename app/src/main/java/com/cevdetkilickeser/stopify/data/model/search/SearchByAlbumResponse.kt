package com.cevdetkilickeser.stopify.data.model.search


import com.google.gson.annotations.SerializedName

data class SearchByAlbumResponse(
    @SerializedName("data")
    val albumDataList: List<AlbumData>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("next")
    val next: String
)