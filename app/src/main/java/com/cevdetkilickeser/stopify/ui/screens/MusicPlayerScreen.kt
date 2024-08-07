package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.viewmodel.VMMusicPlayer

@Composable
fun MusicPlayerScreen(preview: String, title: String, image: String, artistName: String,
    viewModel: VMMusicPlayer = hiltViewModel()
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()

    val sliderPosition = (currentPosition / 1000).toFloat()
    val sliderDuration = (duration / 1000).toFloat()

    LaunchedEffect(key1 = preview) {
        viewModel.load(preview)
        viewModel.play()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = rememberAsyncImagePainter(image) ?: painterResource(id = R.drawable.ic_stopify), contentDescription = "Track Image", modifier = Modifier.size(200.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title ?: "Title",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = artistName ?: "Artist Name",
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Slider(
            value = sliderPosition,
            onValueChange = { newValue ->
                viewModel.seekTo(newValue.toLong() * 1000)
            },
            valueRange = 0f..sliderDuration,
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                inactiveTrackColor = Color.Gray,
                activeTrackColor = Color.Black
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = formatTime(currentPosition))
            Text(text = formatTime(duration))
        }
        
        Spacer(modifier = Modifier.height(50.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /* TODO: Previous song */ }) {
                Icon(painter = painterResource(id = R.drawable.ic_skip_previous), contentDescription = "Previous",
                    modifier = Modifier.size(100.dp))
            }
            IconButton(onClick = { viewModel.rewind() }) {
                Icon(painter = painterResource(id = R.drawable.ic_backward_10), contentDescription = "Rewind 10s")
            }
            IconButton(onClick = { if (isPlaying) viewModel.pause() else viewModel.play() }) {
                Icon(
                    painter = if (isPlaying) painterResource(id = R.drawable.ic_pause) else painterResource(id = R.drawable.ic_play),
                    contentDescription = "Play/Pause",
                    modifier = Modifier.size(100.dp))
            }
            IconButton(onClick = { viewModel.fastForward() }) {
                Icon(painter = painterResource(id = R.drawable.ic_forward_10), contentDescription = "Forward 10s")
            }
            IconButton(onClick = { /* TODO: Next song */ }) {
                Icon(painter = painterResource(id = R.drawable.ic_skip_next), contentDescription = "Next",
                    modifier = Modifier.size(100.dp))
            }
        }

//        IconButton(onClick = { viewModel.downloadSong(preview, title, image, artistName) }) {
//            Icon(Icons.Default.Add, contentDescription = "Download")
//        }
    }
}

fun formatTime(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    return String.format("%d:%02d", minutes, seconds)
}