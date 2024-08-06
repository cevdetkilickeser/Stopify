package com.cevdetkilickeser.stopify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.artist.ArtistAlbumData
import com.cevdetkilickeser.stopify.data.artist.ArtistResponse
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.repo.LikeRepository
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMArtist @Inject constructor(private val serviceRepository: ServiceRepository, private val likeRepository: LikeRepository) : ViewModel() {

    private val _artistState = MutableStateFlow<ArtistResponse?>(null)
    val artistState: StateFlow<ArtistResponse?> = _artistState

    private val _artistAlbumState = MutableStateFlow<List<ArtistAlbumData>>(emptyList())
    val artistAlbumState: StateFlow<List<ArtistAlbumData>> = _artistAlbumState

    fun getArtist(artistId: String) {
        viewModelScope.launch {
            _artistState.value = serviceRepository.getArtistResponse(artistId)
        }
    }

    fun getArtistArtistAlbum(artistId: String) {
        viewModelScope.launch {
            _artistAlbumState.value = serviceRepository.getArtistAlbum(artistId)
        }
    }
}