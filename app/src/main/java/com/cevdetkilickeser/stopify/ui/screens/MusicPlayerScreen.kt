package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cevdetkilickeser.stopify.viewmodel.VMMusicPlayer

@Composable
fun MusicPlayerScreen(preview: String,
    viewModel: VMMusicPlayer = hiltViewModel()
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()

    val sliderPosition = (currentPosition / 1000).toFloat()
    val sliderDuration = (duration / 1000).toFloat()

    LaunchedEffect(key1 = preview) {
        viewModel.load(preview)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Slider(
            value = sliderPosition,
            onValueChange = { newValue ->
                viewModel.pause()
                viewModel.seekTo(newValue.toLong() * 1000)
            },
            onValueChangeFinished = {
                viewModel.play()
            },
            valueRange = 0f..sliderDuration,
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                inactiveTrackColor = Color.Gray,
                activeTrackColor = Color.Black
            ),
            modifier = Modifier.padding(horizontal = 15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = formatTime(currentPosition))
            Text(text = formatTime(duration))
        }
        
        Spacer(modifier = Modifier.height(50.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /* TODO: Previous song */ }) {
                Icon(Icons.Default.Home, contentDescription = "Previous")
            }
            IconButton(onClick = { viewModel.rewind() }) {
                Icon(Icons.Default.Search, contentDescription = "Rewind 5s")
            }
            IconButton(onClick = { if (isPlaying) viewModel.pause() else viewModel.play() }) {
                Icon(if (isPlaying) Icons.Default.Lock else Icons.Default.PlayArrow, contentDescription = "Play/Pause")
            }
            IconButton(onClick = { viewModel.fastForward() }) {
                Icon(Icons.Default.Favorite, contentDescription = "Forward 5s")
            }
            IconButton(onClick = { /* TODO: Next song */ }) {
                Icon(Icons.Default.Build, contentDescription = "Next")
            }
        }

        IconButton(onClick = { viewModel.downloadSong(preview) }) {
            Icon(Icons.Default.Add, contentDescription = "Download")
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    return String.format("%d:%02d", minutes, seconds)
}