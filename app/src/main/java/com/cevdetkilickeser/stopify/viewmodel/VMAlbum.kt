package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.NetworkMonitor
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.album.AlbumResponse
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.isInternetAvailable
import com.cevdetkilickeser.stopify.repo.LikeRepository
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMAlbum @Inject constructor(
    application: Application,
    private val serviceRepository: ServiceRepository,
    private val likeRepository: LikeRepository,
    private val networkMonitor: NetworkMonitor
) : AndroidViewModel(application) {

    private val _albumState = MutableStateFlow<AlbumResponse?>(null)
    val albumState: StateFlow<AlbumResponse?> = _albumState

    private val _playerTrackListState = MutableStateFlow<List<PlayerTrack>>(emptyList())
    val playerTrackListState: StateFlow<List<PlayerTrack>> = _playerTrackListState

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

    fun getAlbum(albumId: String) {
        viewModelScope.launch {
            try {
                _loadingState.value = true
                _albumState.value = serviceRepository.getAlbumResponse(albumId)
                _playerTrackListState.value = _albumState.value!!.tracks.trackDataList
                    .filter { track -> track.preview.isNotEmpty() }
                    .map {
                        PlayerTrack(
                            it.id,
                            it.title,
                            it.preview,
                            it.trackDataAlbum.coverXl,
                            it.trackDataArtist.name
                        )
                    }
                _loadingState.value = false
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
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

    override fun onCleared() {
        super.onCleared()
        networkMonitor.stopNetworkCallback()
    }
}