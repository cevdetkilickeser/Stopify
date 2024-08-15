package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.entity.Download
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.repo.DownloadRepository
import com.cevdetkilickeser.stopify.repo.LikeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VMDownloads @Inject constructor(
    application: Application,
    private val downloadRepository: DownloadRepository,
    private val likeRepository: LikeRepository
): AndroidViewModel(application) {

    private val downloadManager =
        application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    private val _downloadListState = MutableStateFlow<List<Download>>(emptyList())
    val downloadListState: StateFlow<List<Download>> = _downloadListState

    private val _likeListState = MutableStateFlow<List<Like>>(emptyList())
    val likeListState: StateFlow<List<Like>> = _likeListState

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getDownloads(userId: String) {
        viewModelScope.launch {
            try {
                _downloadListState.value = downloadRepository.getDownloads(userId).sortedByDescending { it.downloadId }
                _loadingState.value = false
                _errorState.value = null
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
            }
        }
    }

    fun deleteDownload(downloadId: Long, fileUri: String, userId: String) {
        val file = Uri.parse(fileUri).path?.let { File(it) }
        file?.let {
            if (it.exists()) {
                println("exist")
                it.delete()
            }
        }
        downloadManager.remove(downloadId)
        viewModelScope.launch {
            downloadRepository.deleteDownload(Download(downloadId,"","","","","","",""))
            _downloadListState.value = downloadRepository.getDownloads(userId)
        }
    }

    fun getLikes(userId: String) {
        viewModelScope.launch {
            _likeListState.value = likeRepository.getLikes(userId)
        }
    }

    fun insertLike(like: Like) {
        viewModelScope.launch {
            likeRepository.insertLike(like)
            getLikes(like.userId)
        }
    }

    fun deleteLikeByTrackId(userId: String, trackId: String) {
        viewModelScope.launch {
            likeRepository.deleteLikeByTrackId(userId, trackId)
            getLikes(userId)
        }
    }
}