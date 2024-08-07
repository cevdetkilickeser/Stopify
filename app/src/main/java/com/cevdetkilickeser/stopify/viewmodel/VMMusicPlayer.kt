package com.cevdetkilickeser.stopify.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class VMMusicPlayer @Inject constructor(application: Application
) : AndroidViewModel(application) {

    private val context by lazy { application.applicationContext }

    private val _player: ExoPlayer = ExoPlayer.Builder(application).build()
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration

    init {
        _player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> _duration.value = _player.duration
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
    }

    fun fastForward() {
        _player.seekForward()
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
        _player.release()
    }

    fun downloadSong(previewUrl: String) {
        viewModelScope.launch {
            try {
                val fileName = "downloaded_song.mp3" // Dosya adı burada belirlenebilir
                val file = File(context.filesDir, fileName)

                val urlConnection = URL(previewUrl).openConnection() as HttpURLConnection
                urlConnection.inputStream.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }


                Toast.makeText(context, "Şarkı indirildi!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {

                Toast.makeText(context, "İndirme başarısız oldu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}