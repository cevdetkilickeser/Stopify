package com.cevdetkilickeser.stopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.model.artist.ArtistAlbumData
import com.cevdetkilickeser.stopify.data.model.artist.ArtistResponse
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMArtist @Inject constructor(private val serviceRepository: ServiceRepository) : ViewModel() {

    private val _artistState = MutableStateFlow<ArtistResponse?>(null)
    val artistState: StateFlow<ArtistResponse?> = _artistState

    private val _artistAlbumState = MutableStateFlow<List<ArtistAlbumData>>(emptyList())
    val artistAlbumState: StateFlow<List<ArtistAlbumData>> = _artistAlbumState

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getArtist(artistId: String) {
        viewModelScope.launch {
            try {
                _artistState.value = serviceRepository.getArtistResponse(artistId)
                _loadingState.value = false
                _errorState.value = null
            } catch (e: Exception) {
                _errorState.value = e.message
            }
        }
    }

    fun getArtistArtistAlbum(artistId: String) {
        viewModelScope.launch {
            _artistAlbumState.value = serviceRepository.getArtistAlbum(artistId)
        }
    }
}