package com.cevdetkilickeser.stopify.data.model.genre


import com.cevdetkilickeser.stopify.data.model.genre.GenreData
import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("data")
    val genreDataList: List<GenreData>
)