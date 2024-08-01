package com.cevdetkilickeser.stopify.data.album


import com.google.gson.annotations.SerializedName

data class TrackData(
    @SerializedName("id")
    val id: String,
    @SerializedName("readable")
    val readable: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("title_short")
    val titleShort: String,
    @SerializedName("title_version")
    val titleVersion: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("rank")
    val rank: String,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("explicit_content_lyrics")
    val explicitContentLyrics: Int,
    @SerializedName("explicit_content_cover")
    val explicitContentCover: Int,
    @SerializedName("preview")
    val preview: String,
    @SerializedName("md5_image")
    val md5Image: String,
    @SerializedName("albumArtist")
    val trackDataArtist: TrackDataArtist,
    @SerializedName("trackDataAlbum")
    val trackDataAlbum: TrackDataAlbum,
    @SerializedName("type")
    val type: String
)