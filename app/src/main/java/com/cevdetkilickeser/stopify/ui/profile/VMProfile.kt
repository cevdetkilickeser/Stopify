package com.cevdetkilickeser.stopify.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.model.playlist.UserPlaylistResponse
import com.cevdetkilickeser.stopify.repo.UserPlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMProfile @Inject constructor(
    application: Application,
    private val userPlaylistRepository: UserPlaylistRepository
): AndroidViewModel(application) {

    private val _userPlaylistResponsesState =
        MutableStateFlow<List<UserPlaylistResponse>>(emptyList())
    val userPlaylistResponsesState: StateFlow<List<UserPlaylistResponse>> =
        _userPlaylistResponsesState

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getUserPlaylistResponses(userId: String) {
        try {
            viewModelScope.launch {
                _userPlaylistResponsesState.value = userPlaylistRepository.getUserPlaylistResponses(userId)
                _loadingState.value = false
            }
        } catch (e: Exception) {
            _errorState.value = getApplication<Application>().getString(R.string.error)
        }
    }
}