package com.cevdetkilickeser.stopify.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cevdetkilickeser.stopify.data.entity.History


@Dao
interface HistoryDao {

    @Insert
    suspend fun insertHistory(history: History)

    @Query("SELECT * FROM history_table WHERE trackTitle IS NOT NULL")
    suspend fun getTrackHistory(): List<History>

    @Query("SELECT * FROM history_table WHERE artistId IS NOT NULL")
    suspend fun getArtistHistory(): List<History>

    @Query("SELECT * FROM history_table WHERE albumId IS NOT NULL")
    suspend fun getAlbumHistory(): List<History>

    @Delete
    suspend fun deleteHistory(history: History)
}