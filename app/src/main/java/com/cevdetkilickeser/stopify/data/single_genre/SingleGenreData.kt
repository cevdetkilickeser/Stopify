package com.cevdetkilickeser.stopify.data.single_genre


import com.google.gson.annotations.SerializedName

data class SingleGenreData(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("picture_small")
    val pictureSmall: String,
    @SerializedName("picture_medium")
    val pictureMedium: String,
    @SerializedName("picture_big")
    val pictureBig: String,
    @SerializedName("picture_xl")
    val pictureXl: String,
    @SerializedName("tracklist")
    val trackList: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("type")
    val type: String
)