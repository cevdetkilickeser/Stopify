package com.cevdetkilickeser.stopify.data.artist


import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("share")
    val share: String,
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
    @SerializedName("nb_album")
    val nbAlbum: Int,
    @SerializedName("nb_fan")
    val nbFan: Int,
    @SerializedName("radio")
    val radio: Boolean,
    @SerializedName("tracklist")
    val trackList: String,
    @SerializedName("type")
    val type: String
)