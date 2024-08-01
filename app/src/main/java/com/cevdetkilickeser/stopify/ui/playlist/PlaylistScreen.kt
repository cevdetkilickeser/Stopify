package com.cevdetkilickeser.stopify.ui.playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.cevdetkilickeser.stopify.ui.search.TrackList
import com.cevdetkilickeser.stopify.viewmodel.VMPlaylist

@Composable
fun PlaylistScreen(playlistId: String, viewModel: VMPlaylist = hiltViewModel()) {
    LaunchedEffect(key1 = playlistId) {
        viewModel.getPlaylistDataList(playlistId)
    }
    val trackList by viewModel.state.collectAsState()
    TrackList(trackList = trackList, onTrackClick = {})
}

