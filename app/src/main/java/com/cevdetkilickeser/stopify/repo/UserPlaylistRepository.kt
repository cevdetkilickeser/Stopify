package com.cevdetkilickeser.stopify.repo

import com.cevdetkilickeser.stopify.data.entity.UserPlaylistTrack
import com.cevdetkilickeser.stopify.data.model.playlist.UserPlaylistResponse
import com.cevdetkilickeser.stopify.room.UserPlaylistDao

class UserPlaylistRepository(private val userPlaylistDao: UserPlaylistDao) {

    suspend fun getUserPlaylistResponses(userId: String): List<UserPlaylistResponse> = userPlaylistDao.getUserPlaylistResponses(userId)

    suspend fun insertTrackToUserPlaylist(userPlaylistTrack: UserPlaylistTrack) = userPlaylistDao.insertTrackToUserPlaylist(userPlaylistTrack)

    suspend fun getUserPlaylist(userId: String, userPlaylistId: Int): List<UserPlaylistTrack> = userPlaylistDao.getUserPlaylist(userId, userPlaylistId)

    suspend fun deleteTrackFromUserPlaylist(userPlaylistTrack: UserPlaylistTrack) =  userPlaylistDao.deleteTrackFromUserPlaylist(userPlaylistTrack)
}