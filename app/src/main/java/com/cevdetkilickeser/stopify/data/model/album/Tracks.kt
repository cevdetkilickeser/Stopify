package com.cevdetkilickeser.stopify.data.model.album


import com.google.gson.annotations.SerializedName

data class Tracks(
    @SerializedName("data")
    val trackDataList: List<TrackData>
)