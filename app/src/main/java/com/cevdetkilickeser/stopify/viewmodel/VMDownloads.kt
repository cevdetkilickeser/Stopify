package com.cevdetkilickeser.stopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.entity.Download
import com.cevdetkilickeser.stopify.repo.DownloadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMDownloads @Inject constructor(private val downloadRepository: DownloadRepository): ViewModel() {

    private val _downloadListState = MutableStateFlow<List<Download>>(emptyList())
    val downloadListState: StateFlow<List<Download>> = _downloadListState

    fun getDownloads() {
        viewModelScope.launch {
            _downloadListState.value = downloadRepository.getDownloads()
        }
    }
}