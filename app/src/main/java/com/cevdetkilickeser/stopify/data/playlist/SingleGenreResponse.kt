package com.cevdetkilickeser.stopify.data.playlist


import com.google.gson.annotations.SerializedName

data class SingleGenreResponse(
    @SerializedName("data")
    val singleGenreData: List<SingleGenreData>
)