package com.cevdetkilickeser.stopify.data.album


import com.google.gson.annotations.SerializedName

data class Tracks(
    @SerializedName("data")
    val trackDataList: List<TrackData>
)