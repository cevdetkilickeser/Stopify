package com.cevdetkilickeser.stopify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.playlist.Track
import com.cevdetkilickeser.stopify.repo.LikeRepository
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMPlaylist @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val likeRepository: LikeRepository
) : ViewModel() {

    private val _trackListState = MutableStateFlow<List<Track>>(emptyList())
    val trackListState: StateFlow<List<Track>> = _trackListState

    private val _likeListState = MutableStateFlow<List<Like>>(emptyList())
    val likeListState: StateFlow<List<Like>> = _likeListState

    fun getPlaylistDataList(playlistId: String) {
        viewModelScope.launch {
            try {
                val trackList = serviceRepository.getTrackList(playlistId)
                _trackListState.value = trackList
            } catch (e: Exception) {
                Log.e("ŞŞŞ", "Hata")
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
}