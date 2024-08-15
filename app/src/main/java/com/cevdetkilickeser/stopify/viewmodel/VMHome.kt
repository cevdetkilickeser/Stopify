package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.NetworkMonitor
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.model.genre.GenreData
import com.cevdetkilickeser.stopify.isInternetAvailable
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMHome @Inject constructor(
    application: Application,
    private val repository: ServiceRepository,
    private val networkMonitor: NetworkMonitor
) : AndroidViewModel(application) {

    private val _genreDataState = MutableStateFlow<List<GenreData>>(emptyList())
    val genreDataState: StateFlow<List<GenreData>> = _genreDataState

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

    fun getGenreDataList() {
        viewModelScope.launch {
            try {
                _loadingState.value = true
                _genreDataState.value = repository.getGenreDataList()
                _loadingState.value = false
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