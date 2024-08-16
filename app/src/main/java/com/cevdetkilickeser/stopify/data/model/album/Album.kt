package com.cevdetkilickeser.stopify.data.model.album

data class Album(
    val id: String,
    val title: String,
    val image: String,
    val artist: String,
    val historyId: Int? = null
)
