package com.cevdetkilickeser.stopify.repo

import com.cevdetkilickeser.stopify.data.entity.Download
import com.cevdetkilickeser.stopify.room.DownloadDao

class DownloadRepository(private val downloadDao: DownloadDao) {

    suspend fun insertDownload(download: Download) = downloadDao.insertDownload(download)

    suspend fun deleteDownload(download: Download)= downloadDao.deleteDownload(download)

    suspend fun getDownloads(userId: String): List<Download> = downloadDao.getDownloads(userId)
}