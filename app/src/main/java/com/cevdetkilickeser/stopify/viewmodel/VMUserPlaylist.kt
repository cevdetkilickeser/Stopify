package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.entity.UserPlaylistTrack
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.repo.UserPlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMUserPlaylist @Inject constructor(
    application: Application,
    private val userPlaylistRepository: UserPlaylistRepository
): AndroidViewModel(application) {

    private val _playerTrackListState = MutableStateFlow<List<PlayerTrack>>(emptyList())
    val playerTrackListState: StateFlow<List<PlayerTrack>> = _playerTrackListState

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getUserPlaylist(userId: String, userPlaylistId: Int) {
        viewModelScope.launch {
            try {
                _loadingState.value = true
                _playerTrackListState.value = userPlaylistRepository.getUserPlaylist(userId, userPlaylistId)
                    .sortedByDescending { it.id }
                    .map {
                        PlayerTrack(
                            it.trackId,
                            it.trackTitle,
                            it.trackPreview,
                            it.trackImage,
                            it.trackArtistName,
                            userPlaylistTableId = it.id,
                            userPlaylistId = it.userPlaylistId
                        )
                }
                _loadingState.value = false
            } catch (e:Exception) {
                _errorState.value = getApplication<Application>().getString(R.string.error)
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