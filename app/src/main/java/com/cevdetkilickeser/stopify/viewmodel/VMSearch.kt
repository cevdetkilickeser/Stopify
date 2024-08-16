package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.NetworkMonitor
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.entity.History
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.data.model.search.AlbumData
import com.cevdetkilickeser.stopify.data.model.search.ArtistData
import com.cevdetkilickeser.stopify.isInternetAvailable
import com.cevdetkilickeser.stopify.repo.HistoryRepository
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMSearch @Inject constructor(
    application: Application,
    private val networkMonitor: NetworkMonitor,
    private val serviceRepository: ServiceRepository,
    private val historyRepository: HistoryRepository
) : AndroidViewModel(application) {

    private val _playerTrackListState = MutableStateFlow<List<PlayerTrack>>(emptyList())
    val playerTrackListState: StateFlow<List<PlayerTrack>> = _playerTrackListState

    private val _artistListState = MutableStateFlow<List<ArtistData>>(emptyList())
    val artistListState: StateFlow<List<ArtistData>> = _artistListState

    private val _albumListState = MutableStateFlow<List<AlbumData>>(emptyList())
    val albumListState: StateFlow<List<AlbumData>> = _albumListState

    private val _historyPlayerTrackListState = MutableStateFlow<List<PlayerTrack>>(emptyList())
    val historyPlayerTrackListState: StateFlow<List<PlayerTrack>> = _historyPlayerTrackListState

    private val _historyArtistListState = MutableStateFlow<List<History>>(emptyList())
    val historyArtistListState: StateFlow<List<History>> = _historyArtistListState

    private val _historyAlbumListState = MutableStateFlow<List<History>>(emptyList())
    val historyAlbumListState: StateFlow<List<History>> = _historyAlbumListState

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

    fun search(searchQuery: String, selectedFilter: String) {
        viewModelScope.launch {
            if (searchQuery.isNotEmpty()) {
                try {
                    _loadingState.value = true
                    when (selectedFilter) {
                        "Track" -> getSearchResponse(searchQuery)
                        "Artist" -> getSearchByArtistResponse(searchQuery)
                        "Album" -> getSearchByAlbumResponse(searchQuery)
                    }
                    _loadingState.value = false
                } catch (e:Exception){
                    _errorState.value = getApplication<Application>().getString(R.string.error)
                }
            } else {
                clearSearchResult()
            }
        }
    }

    private fun clearSearchResult() {
        _playerTrackListState.value = emptyList()
        _artistListState.value = emptyList()
        _albumListState.value = emptyList()
    }

    private fun getSearchResponse(query: String) {
        viewModelScope.launch {
            try {
                _playerTrackListState.value = serviceRepository.getSearchResponse(query)
                    .filter { track -> track.preview.isNotEmpty() }
                    .map {
                        PlayerTrack(
                            it.id,
                            it.title,
                            it.preview,
                            it.album.coverXl,
                            it.artist.name
                        )
                    }
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
            }
        }
    }

    private fun getSearchByArtistResponse(query: String) {
        viewModelScope.launch {
            try {
                val artistList = serviceRepository.getSearchByArtistResponse(query)
                _artistListState.value = artistList
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
            }
        }
    }

    private fun getSearchByAlbumResponse(query: String) {
        viewModelScope.launch {
            try {
                val albumList = serviceRepository.getSearchByAlbumResponse(query)
                _albumListState.value = albumList
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
            }
        }
    }

    fun getHistory(selectedFilter: String) {
        viewModelScope.launch {
            try {
                when (selectedFilter) {
                    "Track" -> _historyPlayerTrackListState.value = historyRepository.getTrackHistory()
                        .sortedByDescending { it.historyId }
                        .map {
                            PlayerTrack(
                                it.trackId!!,
                                it.trackTitle!!,
                                it.trackPreview!!,
                                it.trackImage!!,
                                it.trackArtistName!!,
                                historyId = it.historyId
                            )
                        }

                    "Artist" -> _historyArtistListState.value =
                        historyRepository.getArtistHistory().sortedByDescending { it.historyId }

                    "Album" -> _historyAlbumListState.value =
                        historyRepository.getAlbumHistory().sortedByDescending { it.historyId }
                }
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
            }
        }
    }

    fun insertHistory(history: History) {
        viewModelScope.launch {
            historyRepository.insertHistory(history)
        }
    }

    fun deleteHistory(history: History, selectedFilter: String) {
        viewModelScope.launch {
            historyRepository.deleteHistory(history)
            getHistory(selectedFilter)
        }
    }

    override fun onCleared() {
        super.onCleared()
        networkMonitor.stopNetworkCallback()
    }
}

