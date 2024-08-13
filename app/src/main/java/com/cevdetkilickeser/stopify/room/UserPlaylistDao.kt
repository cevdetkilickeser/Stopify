package com.cevdetkilickeser.stopify.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cevdetkilickeser.stopify.data.entity.UserPlaylistTrack
import com.cevdetkilickeser.stopify.data.model.playlist.UserPlaylistResponse

@Dao
interface UserPlaylistDao {

    @Query("SELECT DISTINCT userPlaylistId, userPlaylistName FROM user_playlist_table WHERE userId = :userId")
    suspend fun getUserPlaylistResponses(userId: String): List<UserPlaylistResponse>

    @Insert
    suspend fun insertTrackToUserPlaylist(userPlaylistTrack: UserPlaylistTrack)

    @Delete
    suspend fun deleteTrackFromUserPlaylist(userPlaylistTrack: UserPlaylistTrack)

    @Query("SELECT * FROM user_playlist_table WHERE userId = :userId AND userPlaylistId = :userPlaylistId")
    suspend fun getUserPlaylist(userId: String, userPlaylistId: Int): List<UserPlaylistTrack>
}