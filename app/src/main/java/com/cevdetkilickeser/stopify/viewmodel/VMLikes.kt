package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.repo.LikeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMLikes @Inject constructor(
    application: Application,
    private val likeRepository: LikeRepository
) : AndroidViewModel(application) {

    private val _likeListState = MutableStateFlow<List<Like>>(emptyList())
    val likeListState: StateFlow<List<Like>> = _likeListState

    private val _playerTrackListState = MutableStateFlow<List<PlayerTrack>>(emptyList())
    val playerTrackListState: StateFlow<List<PlayerTrack>> = _playerTrackListState

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getLikes(userId: String) {
        viewModelScope.launch {
            try {
                _likeListState.value = likeRepository.getLikes(userId)
                _playerTrackListState.value = _likeListState.value
                    .sortedByDescending { it.likeId }
                    .map {PlayerTrack(
                        it.trackId,
                        it.trackTitle,
                        it.trackPreview,
                        it.trackImage,
                        it.trackArtistName
                    )
                }
                _loadingState.value = false
                _errorState.value = null
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
            }
        }
    }

    fun deleteLikeByTrackId(userId: String, trackId: String) {
        viewModelScope.launch {
            likeRepository.deleteLikeByTrackId(userId, trackId)
            getLikes(userId)
        }
    }
}