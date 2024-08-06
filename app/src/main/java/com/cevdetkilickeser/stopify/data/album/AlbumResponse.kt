package com.cevdetkilickeser.stopify.data.album


import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("upc")
    val upc: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("share")
    val share: String,
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
    @SerializedName("genres")
    val genres: Genres,
    @SerializedName("label")
    val label: String,
    @SerializedName("nb_tracks")
    val nbTracks: Int,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("fans")
    val fans: Int,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("record_type")
    val recordType: String,
    @SerializedName("available")
    val available: Boolean,
    @SerializedName("tracklist")
    val trackList: String,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("explicit_content_lyrics")
    val explicitContentLyrics: Int,
    @SerializedName("explicit_content_cover")
    val explicitContentCover: Int,
    @SerializedName("contributors")
    val contributors: List<Contributor>,
    @SerializedName("artist")
    val albumArtist: AlbumArtist,
    @SerializedName("type")
    val type: String,
    @SerializedName("tracks")
    val tracks: Tracks
)