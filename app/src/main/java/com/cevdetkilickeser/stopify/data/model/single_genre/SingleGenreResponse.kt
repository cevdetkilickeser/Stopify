package com.cevdetkilickeser.stopify.data.model.single_genre


import com.google.gson.annotations.SerializedName

data class SingleGenreResponse(
    @SerializedName("data")
    val singleGenreDataList: List<SingleGenreData>
)