package com.cevdetkilickeser.stopify.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_table")
data class Download(
    @PrimaryKey(autoGenerate = true) val downloadId: Int = 0,
    @ColumnInfo(name = "trackPreview") val trackPreview: String,
    @ColumnInfo(name = "trackTitle") val trackTitle: String,
    @ColumnInfo(name = "trackImage") val trackImage: String,
    @ColumnInfo(name = "trackArtistName") val trackArtistName: String
)