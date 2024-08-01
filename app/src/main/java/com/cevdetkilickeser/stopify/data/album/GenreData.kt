package com.cevdetkilickeser.stopify.data.album


import com.google.gson.annotations.SerializedName

data class GenreData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("type")
    val type: String
)