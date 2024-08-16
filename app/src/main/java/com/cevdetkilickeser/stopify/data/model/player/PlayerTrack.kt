package com.cevdetkilickeser.stopify.data.model.player

import java.io.Serializable

@kotlinx.serialization.Serializable
data class PlayerTrack(
    val trackId: String,
    val trackTitle: String,
    val trackPreview: String,
    val trackImage: String,
    val trackArtistName: String,
    val downloadId: Long? = null,
    val fileUri: String? = null,
    val userPlaylistTableId: Int? = null,
    val userPlaylistId: Int? = null,
    val historyId: Int? = null
) : Serializable
