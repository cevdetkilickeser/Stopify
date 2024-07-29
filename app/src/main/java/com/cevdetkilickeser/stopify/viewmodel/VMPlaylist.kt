package com.cevdetkilickeser.stopify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.playlist.Track
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMPlaylist @Inject constructor(private val repository: ServiceRepository) : ViewModel() {
    private val _state = MutableStateFlow<List<Track>>(emptyList())
    val state: StateFlow<List<Track>> = _state

    fun getPlaylistDataList(playlistId: String) {
        viewModelScope.launch {
            try {
                val trackList = repository.trackList(playlistId)
                _state.value = trackList
            } catch (e: Exception) {
                Log.e("ŞŞŞ", "Hata")
            }
        }
    }
}