package com.cevdetkilickeser.stopify.ui.screens

import android.annotation.SuppressLint
import androidx.annotation.OptIn
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.ui.component.PlayerTrackList
import com.cevdetkilickeser.stopify.viewmodel.VMMusicPlayer


@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    startIndex: Int, playerTrackList: List<PlayerTrack>, viewModel: VMMusicPlayer = hiltViewModel()
) {
    val context = LocalContext.current
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val isDownload by viewModel.isDownloadState.collectAsState()
    val currentTrack by viewModel.currentTrack.collectAsState()
    //val playerList by viewModel.playerListState.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val sliderPosition = (currentPosition / 1000).toFloat()
    val sliderDuration = (duration / 1000).toFloat()

    LaunchedEffect(startIndex, playerTrackList) {
        viewModel.getDownloads(playerTrackList[startIndex].trackId)
        viewModel.load(startIndex, playerTrackList)
        viewModel.play()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = rememberAsyncImagePainter(currentTrack?.trackImage),
                contentDescription = "Track Image",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = currentTrack?.trackTitle ?: "Title",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = currentTrack?.trackArtistName ?: "Artist Name",
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { currentTrack?.let { viewModel.downloadSong(it) } }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = "Download",
                        tint = if (isDownload) {
                            Color.Green
                        } else {
                            Color.Black
                        },
                        modifier = Modifier.size(32.dp)
                    )
                }

                IconButton(onClick = { }) {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = "Add",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatTime(currentPosition))
                Text(text = formatTime(duration))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { viewModel.previousSong() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skip_previous),
                        contentDescription = "Previous",
                        modifier = Modifier.size(200.dp)
                    )
                }
                IconButton(onClick = { viewModel.rewind() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_backward_10),
                        contentDescription = "Rewind 10s",
                        modifier = Modifier.size(200.dp)
                    )
                }
                IconButton(onClick = { if (isPlaying) viewModel.pause() else viewModel.play() }) {
                    Icon(
                        painter = if (isPlaying) painterResource(id = R.drawable.ic_pause) else painterResource(
                            id = R.drawable.ic_play
                        ),
                        contentDescription = "Play/Pause",
                        modifier = Modifier.size(200.dp)
                    )
                }
                IconButton(onClick = { viewModel.fastForward() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_forward_10),
                        contentDescription = "Forward 10s",
                        modifier = Modifier.size(200.dp)
                    )
                }
                IconButton(onClick = { viewModel.nextSong() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skip_next),
                        contentDescription = "Next",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    if (currentTrack != null) {
                        val shareLink = currentTrack!!.trackPreview
                        viewModel.share(shareLink, context)
                    }
                }) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(32.dp)
                    )
                }

                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Playlist",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.Gray,
            modifier = Modifier.padding(top = 64.dp)
        ) {
            var currentTrackId by remember {
                mutableStateOf(currentTrack?.trackId)
            }
            PlayerTrackList(
                playerList = playerTrackList,
                onClick = { playerTrack ->
                    if (playerTrack.trackId == currentTrack?.trackId) {
                        if (isPlaying) {viewModel.pause()} else {viewModel.play()}
                    } else {
                        viewModel.seekTo(playerTrackList.indexOf(playerTrack), 0L)
                    }
                    currentTrackId = playerTrack.trackId
                },
                currentTrackId = currentTrackId!!
            )
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatTime(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    return String.format("%d:%02d", minutes, seconds)
}