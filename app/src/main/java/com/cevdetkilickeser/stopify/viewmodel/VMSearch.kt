package com.cevdetkilickeser.stopify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.entity.History
import com.cevdetkilickeser.stopify.data.playlist.Track
import com.cevdetkilickeser.stopify.data.search.AlbumData
import com.cevdetkilickeser.stopify.data.search.ArtistData
import com.cevdetkilickeser.stopify.repo.HistoryRepository
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMSearch @Inject constructor(private val repository: ServiceRepository, private val historyRepository: HistoryRepository) : ViewModel() {
    private val _trackListState = MutableStateFlow<List<Track>>(emptyList())
    val trackListState: StateFlow<List<Track>> = _trackListState

    private val _artistListState = MutableStateFlow<List<ArtistData>>(emptyList())
    val artistListState: StateFlow<List<ArtistData>> = _artistListState

    private val _albumListState = MutableStateFlow<List<AlbumData>>(emptyList())
    val albumListState: StateFlow<List<AlbumData>> = _albumListState

    private val _historyListState = MutableStateFlow<List<History>>(emptyList())
    val historyListState: StateFlow<List<History>> = _historyListState

    fun clearSearchResult() {
        _trackListState.value = emptyList()
        _artistListState.value = emptyList()
        _albumListState.value = emptyList()
    }

    fun getSearchResponse(query: String) {
        viewModelScope.launch {
            try {
                val trackList = repository.getSearchResponse(query)
                _trackListState.value = trackList
            } catch (e: Exception) {
                Log.e("ŞŞŞ", "Hata")
            }
        }
    }

    fun getSearchByArtistResponse(query: String) {
        viewModelScope.launch {
            try {
                val artistList = repository.getSearchByArtistResponse(query)
                _artistListState.value = artistList
            } catch (e: Exception) {
                Log.e("ŞŞŞ", "Hata")
            }
        }
    }

    fun getSearchByAlbumResponse(query: String) {
        viewModelScope.launch {
            try {
                val albumList = repository.getSearchByAlbumResponse(query)
                _albumListState.value = albumList
            } catch (e: Exception) {
                Log.e("ŞŞŞ", "Hata")
            }
        }
    }

    fun getHistory(type: String) {
        viewModelScope.launch {
            _historyListState.value = emptyList()
            try {
                when(type) {
                    "track" -> _historyListState.value = historyRepository.getTrackHistory()
                    "artist" -> _historyListState.value = historyRepository.getArtistHistory()
                    "album" -> _historyListState.value = historyRepository.getAlbumHistory()
                }
            } catch (e:Exception){
                Log.e("ŞŞŞ", "Hata")
            }
        }
    }

    fun insertHistory(history: History) {
        viewModelScope.launch {
            historyRepository.insertHistory(history)
        }
    }
}