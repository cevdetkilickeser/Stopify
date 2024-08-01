package com.cevdetkilickeser.stopify.viewmodel

import androidx.lifecycle.ViewModel
import com.cevdetkilickeser.stopify.data.album.AlbumResponse
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VMAlbum @Inject constructor(private val serviceRepository: ServiceRepository) : ViewModel() {
    private val _state = MutableStateFlow<List<AlbumResponse>>(emptyList())
    val state: StateFlow<List<AlbumResponse>> = _state

}