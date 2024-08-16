package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.cevdetkilickeser.stopify.NetworkMonitor
import com.cevdetkilickeser.stopify.data.entity.Download
import com.cevdetkilickeser.stopify.data.entity.UserPlaylistTrack
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.data.model.playlist.UserPlaylistResponse
import com.cevdetkilickeser.stopify.isInternetAvailable
import com.cevdetkilickeser.stopify.repo.DownloadRepository
import com.cevdetkilickeser.stopify.repo.UserPlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class VMMusicPlayer @Inject constructor(
    application: Application,
    private val downloadRepository: DownloadRepository,
    private val userPlaylistRepository: UserPlaylistRepository,
    private val networkMonitor: NetworkMonitor
) : AndroidViewModel(application) {

    private val downloadManager =
        application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    private val _player: ExoPlayer = ExoPlayer.Builder(application)
        .setSeekForwardIncrementMs(10000L)
        .setSeekBackIncrementMs(10000L)
        .build()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    private val _playerListState = MutableStateFlow<List<PlayerTrack>>(emptyList())

    private val _currentTrack = MutableStateFlow<PlayerTrack?>(null)
    val currentTrack: StateFlow<PlayerTrack?> = _currentTrack

    private val _downloadListState = MutableStateFlow<List<Download>>(emptyList())

    private val _isDownloadState = MutableStateFlow(false)
    val isDownloadState: StateFlow<Boolean> = _isDownloadState

    private val _isDownloadInfoState = MutableStateFlow("")
    val isDownloadInfoState: StateFlow<String> = _isDownloadInfoState

    private val _userPlaylistResponsesState =
        MutableStateFlow<List<UserPlaylistResponse>>(emptyList())
    val userPlaylistResponsesState: StateFlow<List<UserPlaylistResponse>> =
        _userPlaylistResponsesState

    private val _nextUserPlaylistId = MutableStateFlow(0)
    val nextUserPlaylistId: StateFlow<Int> = _nextUserPlaylistId

    private val _isConnected = MutableStateFlow(isInternetAvailable(application))
    val isConnected: StateFlow<Boolean> = _isConnected


    init {
        networkMonitor.startNetworkCallback()
        networkMonitor.onNetworkStatusChanged = { isConnected ->
            _isConnected.value = isConnected
        }

        _player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val url = mediaItem?.localConfiguration?.uri.toString()
                _currentTrack.value =
                    _playerListState.value.find { playerTrack -> playerTrack.trackPreview == url }
                _isDownloadState.value =
                    _downloadListState.value.any { it.trackId == currentTrack.value?.trackId }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        _duration.value = _player.duration
                        _isPlaying.value = _player.playWhenReady
                    }

                    Player.STATE_ENDED -> {
                        _player.seekTo(0, 0L)
                        _player.pause()
                        _isPlaying.value = false
                        _currentPosition.value = 0L
                    }

                    Player.STATE_BUFFERING -> {}
                    Player.STATE_IDLE -> {}
                }
            }
        })
    }

    fun load(startIndex: Int, playerTrackList: List<PlayerTrack>) {
        _playerListState.value = playerTrackList
        val mediaItems =
            playerTrackList.map { playerTrack -> MediaItem.fromUri(playerTrack.trackPreview) }
        _player.setMediaItems(mediaItems, startIndex, 0L)
        _player.prepare()
    }

    fun play() {
        _player.play()
        _isPlaying.value = true
        startUpdatingPosition()
    }

    fun pause() {
        _player.pause()
        _isPlaying.value = false
    }

    fun nextSong() {
        val currentIndex = _player.currentMediaItemIndex
        if (currentIndex < _player.mediaItemCount - 1) {
            _player.seekTo(currentIndex + 1, 0L)
            _currentPosition.value = 0L
            play()
        }
    }

    fun previousSong() {
        val currentIndex = _player.currentMediaItemIndex
        if (_currentPosition.value > 1000L) {
            _player.seekTo(currentIndex, 0L)
            _currentPosition.value = 0L
        } else {
            if (currentIndex > 0) {
                _player.seekTo(currentIndex - 1, 0L)
                _player.play()
            }
        }
    }

    fun rewind() {
        _player.seekBack()
        if (_currentPosition.value > 10000L) {
            _currentPosition.value -= 10000L
        } else {
            _currentPosition.value = 0L
        }
    }

    fun fastForward() {
        _player.seekForward()
        val remain = _duration.value - _currentPosition.value
        if (remain < 10000L) {
            _currentPosition.value = _duration.value
        } else {
            _currentPosition.value += 10000L
        }
    }

    fun seekTo(position: Long) {
        _player.seekTo(position)
    }

    fun seekTo(index: Int, position: Long) {
        _player.seekTo(index, position)
    }

    fun share(shareLink: String, context: Context) {
        val shareWith = "ShareWith"
        val type = "text/plain"

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = type
        intent.putExtra(Intent.EXTRA_TEXT, shareLink)

        ContextCompat.startActivity(
            context,
            Intent.createChooser(intent, shareWith),
            null
        )
    }

    private fun startUpdatingPosition() {
        viewModelScope.launch {
            while (_isPlaying.value) {
                _currentPosition.value = _player.currentPosition
                delay(1000L)
            }
        }
    }

    fun getDownloadList(userId: String, trackId: String) {
        viewModelScope.launch {
            _downloadListState.value = downloadRepository.getDownloads(userId)
            _isDownloadState.value = _downloadListState.value.any { it.trackId == trackId }
        }
    }

    fun downloadSong(playerTrack: PlayerTrack, userId: String) {
        viewModelScope.launch {
            try {
                val downloadId = downloadRepository.downloadToLocalStorage(playerTrack, downloadManager)
                val fileUri: String? = downloadRepository.getDownloadedFileUriToInsert(downloadManager, downloadId)
                val download = fileUri?.let {
                    Download(
                        0,
                        userId,
                        it,
                        playerTrack.trackId,
                        playerTrack.trackPreview,
                        playerTrack.trackTitle,
                        playerTrack.trackImage,
                        playerTrack.trackArtistName
                    )
                }
                downloadRepository.insertDownload(download!!)
                getDownloadList(userId, playerTrack.trackId)
                _isDownloadInfoState.value = "Downloaded"
                delay(1000)
                _isDownloadInfoState.value = ""
            } catch (e: Exception) {
                _isDownloadInfoState.value = "Could Not download"
                delay(1000)
                _isDownloadInfoState.value = ""
            }
        }
    }

    fun deleteDownload(userId: String, trackId: String, downloadId: Long, fileUri: String, context: Context) {
        viewModelScope.launch {
            downloadRepository.deleteDownloadFromLocaleStorage(downloadId, fileUri, context, downloadManager)
            downloadRepository.deleteDownload(Download(downloadId,"","","","","","",""))
            getDownloadList(userId, trackId)
            _isDownloadInfoState.value = "Deleted"
            delay(1000)
            _isDownloadInfoState.value = ""
        }
    }

    fun getSingleDownload(userId: String, trackId: String): Download {
        return _downloadListState.value.find { it.trackId == trackId && it.userId == userId }!!
    }

    fun getUserPlaylistResponses(userId: String) {
        viewModelScope.launch {
            try {
                _userPlaylistResponsesState.value =
                    userPlaylistRepository.getUserPlaylistResponses(userId)
                _nextUserPlaylistId.value = userPlaylistRepository.getUserPlaylistResponses(userId)
                    .last().userPlaylistId + 1
            } catch (e: Exception) {
                Log.e("MusicPlayerScreen", e.message.toString())
            }
        }
    }

    fun insertTrackToUserPlaylist(userPlaylistTrack: UserPlaylistTrack) {
        viewModelScope.launch {
            userPlaylistRepository.insertTrackToUserPlaylist(userPlaylistTrack)
            getUserPlaylistResponses(userPlaylistTrack.userId)
        }
    }

    fun insertTrackToNewUserPlaylist(userPlaylistTrack: UserPlaylistTrack) {
        viewModelScope.launch {
            userPlaylistRepository.insertTrackToUserPlaylist(userPlaylistTrack)
            getUserPlaylistResponses(userPlaylistTrack.userId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _player.release()
        networkMonitor.stopNetworkCallback()
    }
}