package com.cevdetkilickeser.stopify.repo

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.cevdetkilickeser.stopify.data.entity.Download
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.room.DownloadDao
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DownloadRepository@Inject constructor(
    @ApplicationContext private val context: Context, private val downloadDao: DownloadDao) {

    suspend fun insertDownload(download: Download) = downloadDao.insertDownload(download)

    suspend fun deleteDownload(download: Download)= downloadDao.deleteDownload(download)

    suspend fun getDownloads(userId: String): List<Download> = downloadDao.getDownloads(userId)

    fun deleteDownloadFromLocaleStorage(downloadId: Long, fileUri: String, downloadManager: DownloadManager){
        val uri = Uri.parse(fileUri)
        val resolver = context.contentResolver
        resolver.delete(uri, null, null)
        downloadManager.remove(downloadId)
    }

    fun downloadToLocalStorage(playerTrack: PlayerTrack, downloadManager: DownloadManager): Long {
        val request = Request(Uri.parse(playerTrack.trackPreview))
            .setTitle("${playerTrack.trackTitle.replace(" ", "_")}.mp3")
            .setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
        return downloadManager.enqueue(request)
    }

    fun getDownloadedFileUriToInsert(downloadManager: DownloadManager, downloadId: Long): String? {
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor: Cursor = downloadManager.query(query)

        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
            val uriString = cursor.getString(columnIndex)
            if (uriString != null) {
                return uriString
            }
        }
        cursor.close()
        return null
    }
}