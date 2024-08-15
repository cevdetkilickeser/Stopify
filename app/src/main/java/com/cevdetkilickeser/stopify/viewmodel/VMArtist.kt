package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.NetworkMonitor
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.model.artist.ArtistAlbumData
import com.cevdetkilickeser.stopify.data.model.artist.ArtistResponse
import com.cevdetkilickeser.stopify.isInternetAvailable
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMArtist @Inject constructor(
    application: Application,
    private val serviceRepository: ServiceRepository,
    private val networkMonitor: NetworkMonitor
) : AndroidViewModel(application) {

    private val _artistState = MutableStateFlow<ArtistResponse?>(null)
    val artistState: StateFlow<ArtistResponse?> = _artistState

    private val _artistAlbumState = MutableStateFlow<List<ArtistAlbumData>>(emptyList())
    val artistAlbumState: StateFlow<List<ArtistAlbumData>> = _artistAlbumState

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

    fun getArtist(artistId: String) {
        viewModelScope.launch {
            try {
                _loadingState.value = true
                _artistState.value = serviceRepository.getArtistResponse(artistId)
                _artistAlbumState.value = serviceRepository.getArtistAlbum(artistId)
                _loadingState.value = false
                _errorState.value = null
            } catch (e: Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        networkMonitor.stopNetworkCallback()
    }
}