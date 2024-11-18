package com.cevdetkilickeser.stopify.repo

import com.cevdetkilickeser.stopify.data.entity.History
import com.cevdetkilickeser.stopify.room.HistoryDao
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val historyDao: HistoryDao) {

    suspend fun insertHistory(history: History) = historyDao.insertHistory(history)

    suspend fun getTrackHistory(userId: String): List<History> = historyDao.getTrackHistory(userId)

    suspend fun getArtistHistory(userId: String): List<History> = historyDao.getArtistHistory(userId)

    suspend fun getAlbumHistory(userId: String): List<History> = historyDao.getAlbumHistory(userId)

    suspend fun deleteHistory(history: History) = historyDao.deleteHistory(history)

}