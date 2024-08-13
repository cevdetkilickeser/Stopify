package com.cevdetkilickeser.stopify.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_playlist_table")
data class UserPlaylistTrack(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "userPlaylistId") val userPlaylistId: Int,
    @ColumnInfo(name = "userPlaylistName") val userPlaylistName: String,
    @ColumnInfo(name = "trackId") val trackId: String,
    @ColumnInfo(name = "trackPreview") val trackPreview: String,
    @ColumnInfo(name = "trackTitle") val trackTitle: String,
    @ColumnInfo(name = "trackImage") val trackImage: String,
    @ColumnInfo(name = "trackArtistName") val trackArtistName: String
)
