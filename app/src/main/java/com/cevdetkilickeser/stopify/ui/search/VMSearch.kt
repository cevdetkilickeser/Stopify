package com.cevdetkilickeser.stopify.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.NetworkMonitor
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.entity.History
import com.cevdetkilickeser.stopify.data.model.album.Album
import com.cevdetkilickeser.stopify.data.model.artist.Artist
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
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

    private val _historyPlayerTrackListState = MutableStateFlow<List<PlayerTrack>>(emptyList())
    val historyPlayerTrackListState: StateFlow<List<PlayerTrack>> = _historyPlayerTrackListState

    private val _artistListState = MutableStateFlow<List<Artist>>(emptyList())
    val artistListState: StateFlow<List<Artist>> = _artistListState

    private val _historyArtistListState = MutableStateFlow<List<Artist>>(emptyList())
    val historyArtistListState: StateFlow<List<Artist>> = _historyArtistListState

    private val _albumListState = MutableStateFlow<List<Album>>(emptyList())
    val albumListState: StateFlow<List<Album>> = _albumListState

    private val _historyAlbumListState = MutableStateFlow<List<Album>>(emptyList())
    val historyAlbumListState: StateFlow<List<Album>> = _historyAlbumListState

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
                } catch (e: Exception) {
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
                _artistListState.value = serviceRepository.getSearchByArtistResponse(query)
                    .map {
                        Artist(
                            it.id,
                            it.name,
                            it.pictureXl
                        )
                    }
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
            }
        }
    }

    private fun getSearchByAlbumResponse(query: String) {
        viewModelScope.launch {
            try {
                _albumListState.value = serviceRepository.getSearchByAlbumResponse(query)
                    .map {
                        Album(
                            it.id,
                            it.title,
                            it.coverXl,
                            it.artist.name
                        )
                    }
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
            }
        }
    }

    fun getHistory(userId: String, selectedFilter: String) {
        viewModelScope.launch {
            try {
                when (selectedFilter) {
                    "Track" -> _historyPlayerTrackListState.value =
                        historyRepository.getTrackHistory(userId)
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

                    "Artist" -> _historyArtistListState.value = historyRepository.getArtistHistory(userId)
                        .sortedByDescending { it.historyId }
                        .map {
                            Artist(
                                it.artistId!!,
                                it.artistName!!,
                                it.artistImage!!,
                                it.historyId
                            )
                        }

                    "Album" -> _historyAlbumListState.value = historyRepository.getAlbumHistory(userId)
                        .sortedByDescending { it.historyId }
                        .map {
                            Album(
                                it.albumId!!,
                                it.albumTitle!!,
                                it.albumImage!!,
                                it.albumArtistName!!,
                                it.historyId
                            )
                        }
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
            getHistory(history.userId, selectedFilter)
        }
    }

    override fun onCleared() {
        super.onCleared()
        networkMonitor.stopNetworkCallback()
    }
}

