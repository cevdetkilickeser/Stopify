package com.cevdetkilickeser.stopify.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class History(
    @PrimaryKey(autoGenerate = true) val historyId: Int = 0,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "trackId") val trackId: String? = null,
    @ColumnInfo(name = "trackTitle") val trackTitle: String? = null,
    @ColumnInfo(name = "trackImage") val trackImage: String? = null,
    @ColumnInfo(name = "trackArtistName") val trackArtistName: String? = null,
    @ColumnInfo(name = "trackLink") val trackLink: String? = null,
    @ColumnInfo(name = "artistId") val artistId: String? = null,
    @ColumnInfo(name = "artistName") val artistName: String? = null,
    @ColumnInfo(name = "artistImage") val artistImage: String? = null,
    @ColumnInfo(name = "albumId") val albumId: String? = null,
    @ColumnInfo(name = "albumTitle") val albumTitle: String? = null,
    @ColumnInfo(name = "albumImage") val albumImage: String? = null,
    @ColumnInfo(name = "albumArtistName") val albumArtistName: String? = null,
)
