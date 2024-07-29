package com.cevdetkilickeser.stopify.data.genre


import com.cevdetkilickeser.stopify.data.genre.GenreData
import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("data")
    val genreDataList: List<GenreData>
)