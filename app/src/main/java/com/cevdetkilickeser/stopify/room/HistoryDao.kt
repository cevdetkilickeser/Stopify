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

    @Query("SELECT * FROM history_table WHERE trackTitle IS NOT NULL AND userId = :userId")
    suspend fun getTrackHistory(userId: String): List<History>

    @Query("SELECT * FROM history_table WHERE artistId IS NOT NULL AND userId = :userId")
    suspend fun getArtistHistory(userId: String): List<History>

    @Query("SELECT * FROM history_table WHERE albumId IS NOT NULL AND userId = :userId")
    suspend fun getAlbumHistory(userId: String): List<History>

    @Delete
    suspend fun deleteHistory(history: History)
}