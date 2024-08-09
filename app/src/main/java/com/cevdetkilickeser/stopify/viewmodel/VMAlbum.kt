package com.cevdetkilickeser.stopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.model.album.AlbumResponse
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.repo.LikeRepository
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMAlbum @Inject constructor(private val serviceRepository: ServiceRepository, private val likeRepository: LikeRepository) : ViewModel() {

    private val _albumState = MutableStateFlow<AlbumResponse?>(null)
    val albumState: StateFlow<AlbumResponse?> = _albumState

    private val _likeListState = MutableStateFlow<List<Like>>(emptyList())
    val likeListState: StateFlow<List<Like>> = _likeListState

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getAlbum(albumId: String)  {
        viewModelScope.launch {
            try {
                _albumState.value = serviceRepository.getAlbumResponse(albumId)
                _loadingState.value = false
                _errorState.value = null
            } catch (e: Exception) {
                _errorState.value = e.message
            }
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

    fun getLikes(userId: String) {
        viewModelScope.launch {
            _likeListState.value = likeRepository.getLikes(userId)
        }
    }
}