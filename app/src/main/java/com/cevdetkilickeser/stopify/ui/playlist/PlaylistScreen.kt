package com.cevdetkilickeser.stopify.ui.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.data.playlist.Track
import com.cevdetkilickeser.stopify.viewmodel.VMPlaylist

@Composable
fun PlaylistScreen(playlistId: String, viewModel: VMPlaylist = hiltViewModel()) {
    LaunchedEffect(key1 = playlistId) {
        viewModel.getPlaylistDataList(playlistId)
    }
    val trackList by viewModel.state.collectAsState()
    Playlist(trackList = trackList)
}

@Composable
fun Playlist(trackList: List<Track>) {
    LazyColumn {
        items(trackList) { track ->
            PlaylistRow(track = track)
        }
    }
}

@Composable
fun PlaylistRow(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(track.artist.picture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = track.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = track.artist.name,
                style = MaterialTheme.typography
                    .titleSmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}