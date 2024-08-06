package com.cevdetkilickeser.stopify.data.artist


import com.google.gson.annotations.SerializedName

data class ArtistAlbum(
    @SerializedName("data")
    val artistAlbumDataList: List<ArtistAlbumData>,
    @SerializedName("total")
    val total: Int
)