package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.NetworkMonitor
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.playlist.Track
import com.cevdetkilickeser.stopify.isInternetAvailable
import com.cevdetkilickeser.stopify.repo.LikeRepository
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMPlaylist @Inject constructor(
    application: Application,
    private val serviceRepository: ServiceRepository,
    private val likeRepository: LikeRepository,
    private val networkMonitor: NetworkMonitor
) : AndroidViewModel(application) {

    private val _trackListState = MutableStateFlow<List<Track>>(emptyList())
    val trackListState: StateFlow<List<Track>> = _trackListState

    private val _likeListState = MutableStateFlow<List<Like>>(emptyList())
    val likeListState: StateFlow<List<Like>> = _likeListState

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    private val _isConnected = MutableStateFlow(isInternetAvailable(application))
    val isConnected: StateFlow<Boolean> = _isConnected

    init {
        networkMonitor.startNetworkCallback()
        networkMonitor.onNetworkStatusChanged = { isConnected ->
            _errorState.value = null
            _isConnected.value = isConnected
        }
    }

    fun getPlaylistDataList(playlistId: String) {
        viewModelScope.launch {
            try {
                _loadingState.value = true
                _trackListState.value = serviceRepository.getTrackList(playlistId)
                    .filter { track -> track.preview.isNotEmpty() }
                _loadingState.value = false
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
            }
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

    override fun onCleared() {
        super.onCleared()
        networkMonitor.stopNetworkCallback()
    }
}