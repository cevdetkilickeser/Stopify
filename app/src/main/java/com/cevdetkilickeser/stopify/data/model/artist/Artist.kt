package com.cevdetkilickeser.stopify.data.model.artist

data class Artist(
    val id: String,
    val name: String,
    val image: String,
    val historyId: Int? = null
)
