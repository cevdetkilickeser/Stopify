package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.NetworkMonitor
import com.cevdetkilickeser.stopify.data.model.single_genre.SingleGenreData
import com.cevdetkilickeser.stopify.isInternetAvailable
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMSingleGenre @Inject constructor(
    application: Application,
    private val repository: ServiceRepository,
    private val networkMonitor: NetworkMonitor
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<List<SingleGenreData>>(emptyList())
    val state: StateFlow<List<SingleGenreData>> = _state

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

    fun getSingleGenreDataList(genreId: String) {
        viewModelScope.launch {
            try {
                _loadingState.value = true
                _state.value = repository.getSingleGenreDataList(genreId)
                _loadingState.value = false
            } catch (e: Exception) {
                _errorState.value = "Ops... Something went wrong"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        networkMonitor.stopNetworkCallback()
    }
}