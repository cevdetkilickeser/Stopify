package com.cevdetkilickeser.stopify.repo

import com.cevdetkilickeser.stopify.data.entity.History
import com.cevdetkilickeser.stopify.room.HistoryDao

class HistoryRepository(private val historyDao: HistoryDao) {

    suspend fun insertHistory(history: History) = historyDao.insertHistory(history)

    suspend fun getTrackHistory(): List<History> = historyDao.getTrackHistory()

    suspend fun getArtistHistory(): List<History> = historyDao.getArtistHistory()

    suspend fun getAlbumHistory(): List<History> = historyDao.getAlbumHistory()

    suspend fun deleteHistory(history: History) = historyDao.deleteHistory(history)

    suspend fun deleteTrackHistory() = historyDao.deleteTrackHistory()

    suspend fun deleteArtistHistory() = historyDao.deleteArtistHistory()

    suspend fun deleteAlbumHistory() = historyDao.deleteAlbumHistory()
}