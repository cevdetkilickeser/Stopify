package com.cevdetkilickeser.stopify.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_table")
data class Download(
    @PrimaryKey(autoGenerate = true) val downloadId: Long = 0,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "fileUri") val fileUri: String? = null,
    @ColumnInfo(name = "trackId") val trackId: String,
    @ColumnInfo(name = "trackPreview") val trackPreview: String,
    @ColumnInfo(name = "trackTitle") val trackTitle: String,
    @ColumnInfo(name = "trackImage") val trackImage: String,
    @ColumnInfo(name = "trackArtistName") val trackArtistName: String
)