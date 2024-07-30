package com.cevdetkilickeser.stopify.data.search


import com.cevdetkilickeser.stopify.data.playlist.Artist
import com.google.gson.annotations.SerializedName

data class AlbumData(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("cover")
    val cover: String,
    @SerializedName("cover_small")
    val coverSmall: String,
    @SerializedName("cover_medium")
    val coverMedium: String,
    @SerializedName("cover_big")
    val coverBig: String,
    @SerializedName("cover_xl")
    val coverXl: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("genre_id")
    val genreId: Int,
    @SerializedName("nb_tracks")
    val nbTracks: Int,
    @SerializedName("record_type")
    val recordType: String,
    @SerializedName("tracklist")
    val trackList: String,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("artist")
    val artist: Artist,
    @SerializedName("type")
    val type: String
)