package com.cevdetkilickeser.stopify.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cevdetkilickeser.stopify.data.playlist.Track

@Entity(tableName = "like_table")
data class Like(
    @PrimaryKey(autoGenerate = true) val likeId: Int = 0,
    @ColumnInfo(name = "userId") val userId: String,
    @Embedded val track: Track
)
