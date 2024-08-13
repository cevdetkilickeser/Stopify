package com.cevdetkilickeser.stopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.entity.UserPlaylistTrack
import com.cevdetkilickeser.stopify.repo.UserPlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMUserPlayerList @Inject constructor(private val userPlaylistRepository: UserPlaylistRepository): ViewModel() {

    private val _userPlaylistState = MutableStateFlow<List<UserPlaylistTrack>>(emptyList())
    val userPlaylistState: StateFlow<List<UserPlaylistTrack>> = _userPlaylistState

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getUserPlaylist(userId: String, userPlaylistId: Int) {
        viewModelScope.launch {
            try {
                _userPlaylistState.value = userPlaylistRepository.getUserPlaylist(userId, userPlaylistId)
                _loadingState.value = false
            } catch (e:Exception) {
                _errorState.value = "Ops... Something went wrong"
            }
        }
    }

    fun deleteUserPlaylistTrack(userPlaylistTrack: UserPlaylistTrack) {
        viewModelScope.launch {
            userPlaylistRepository.deleteTrackFromUserPlaylist(userPlaylistTrack)
            getUserPlaylist(userPlaylistTrack.userId, userPlaylistTrack.userPlaylistId)
        }
    }
}