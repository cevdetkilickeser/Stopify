package com.cevdetkilickeser.stopify.data.album


import com.google.gson.annotations.SerializedName

data class TrackDataAlbum(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("cover_small")
    val coverSmall: String,
    @SerializedName("cover_medium")
    val coverMedium: String,
    @SerializedName("cover_big")
    val coverBig: String,
    @SerializedName("cover_xl")
    val coverXl: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("tracklist")
    val trackList: String,
    @SerializedName("type")
    val type: String
)