package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.cevdetkilickeser.stopify.repo.DownloadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class VMMusicPlayer @Inject constructor(application: Application, private val downloadRepository: DownloadRepository, private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    //private val context by lazy { application.applicationContext }

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

    init {
        val savedPosition = savedStateHandle.get<Long>("currentPosition") ?: 0L
        _player.seekTo(savedPosition)

        _player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        _duration.value = _player.duration
                        _isPlaying.value = _player.playWhenReady
                    }
                    Player.STATE_ENDED -> _player.seekTo(0)
                    Player.STATE_BUFFERING -> {

                    }

                    Player.STATE_IDLE -> {

                    }
                }
            }
        })
    }

    fun load(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        _player.setMediaItem(mediaItem)
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

    fun rewind() {
        _player.seekBack()
        if (_currentPosition.value > 10000L){
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

    private fun startUpdatingPosition() {
        viewModelScope.launch {
            while (_isPlaying.value) {
                _currentPosition.value = _player.currentPosition
                delay(1000L)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        savedStateHandle["currentPosition"] = _currentPosition.value
        _player.release()
    }
}

//    fun downloadSong(previewUrl: String, title: String, image: String, artistName: String) {
//        viewModelScope.launch {
//            try {
//                val fileName = "${title.replace(" ", "_")}.mp3"
//                val file = File(context.filesDir, fileName)
//
//                val urlConnection = URL(previewUrl).openConnection() as HttpURLConnection
//                urlConnection.inputStream.use { input ->
//                    FileOutputStream(file).use { output ->
//                        input.copyTo(output)
//                    }
//                }
//
//                val download = Download(0, previewUrl, title, image, artistName)
//                downloadRepository.insertDownload(download)
//
//                Toast.makeText(context, "Download Successfully", Toast.LENGTH_SHORT).show()
//
//            } catch (e: Exception) {
//
//                Toast.makeText(context, "An Error Occur, Try Again", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }