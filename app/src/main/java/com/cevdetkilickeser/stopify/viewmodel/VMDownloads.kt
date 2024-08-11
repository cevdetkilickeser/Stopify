package com.cevdetkilickeser.stopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.entity.Download
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.repo.DownloadRepository
import com.cevdetkilickeser.stopify.repo.LikeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMDownloads @Inject constructor(private val downloadRepository: DownloadRepository, private val likeRepository: LikeRepository): ViewModel() {

    private val _downloadListState = MutableStateFlow<List<Download>>(emptyList())
    val downloadListState: StateFlow<List<Download>> = _downloadListState

    private val _likeListState = MutableStateFlow<List<Like>>(emptyList())
    val likeListState: StateFlow<List<Like>> = _likeListState

    fun getDownloads() {
        viewModelScope.launch {
            _downloadListState.value = downloadRepository.getDownloads().sortedByDescending { it.downloadId }
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