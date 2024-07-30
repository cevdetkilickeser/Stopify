package com.cevdetkilickeser.stopify.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true) val likeId: Int = 0,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "trackName") val trackName: String
)
