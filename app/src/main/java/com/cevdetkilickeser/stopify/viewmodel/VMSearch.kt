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
class VMSearch @Inject constructor(private val repository: ServiceRepository) : ViewModel() {
    private val _state = MutableStateFlow<List<Track>>(emptyList())
    val state: StateFlow<List<Track>> = _state

    fun clearSearchResult() {
        _state.value = emptyList()
    }

    fun getSearchResponse(query: String) {
        viewModelScope.launch {
            try {
                val trackList = repository.getSearchResponse(query)
                _state.value = trackList
                Log.e("şş", "getSearchResponse çalıştı")
            } catch (e: Exception) {
                Log.e("ŞŞŞ", "Hata")
            }
        }
    }

    fun getSearchByArtistResponse(query: String) {
        viewModelScope.launch {
            try {
                val trackList = repository.getSearchResponse(query)
                _state.value = trackList
                Log.e("şş", "getSearchByArtistResponse çalıştı")
            } catch (e: Exception) {
                Log.e("ŞŞŞ", "Hata")
            }
        }
    }

    fun getSearchByAlbumResponse(query: String) {
        viewModelScope.launch {
            try {
                val trackList = repository.getSearchResponse(query)
                _state.value = trackList
                Log.e("şş", "getSearchByAlbumResponse çalıştı")
            } catch (e: Exception) {
                Log.e("ŞŞŞ", "Hata")
            }
        }
    }
}