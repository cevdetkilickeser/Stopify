package com.cevdetkilickeser.stopify.data.album


import com.google.gson.annotations.SerializedName

data class Genres(
    @SerializedName("data")
    val genreDataList: List<GenreData>
)