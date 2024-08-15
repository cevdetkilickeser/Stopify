package com.cevdetkilickeser.stopify.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cevdetkilickeser.stopify.data.entity.Download

@Dao
interface DownloadDao {

    @Insert
    suspend fun insertDownload(download: Download)

    @Delete
    suspend fun deleteDownload(download: Download)

    @Query("SELECT * FROM download_table  WHERE userId = :userId")
    suspend fun getDownloads(userId: String): List<Download>
}