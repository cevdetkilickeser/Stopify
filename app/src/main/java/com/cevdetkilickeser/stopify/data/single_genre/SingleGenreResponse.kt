package com.cevdetkilickeser.stopify.data.single_genre


import com.google.gson.annotations.SerializedName

data class SingleGenreResponse(
    @SerializedName("data")
    val singleGenreDataList: List<SingleGenreData>
)