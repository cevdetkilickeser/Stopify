package com.cevdetkilickeser.stopify.ui.playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.hilt.navigation.compose.hiltViewModel
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.ui.search.TrackList
import com.cevdetkilickeser.stopify.viewmodel.VMPlaylist
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PlaylistScreen(playlistId: String, viewModel: VMPlaylist = hiltViewModel()) {
    val userId = FirebaseAuth.getInstance().currentUser!!.toString()
    LaunchedEffect(key1 = playlistId, key2 = userId) {
        viewModel.getPlaylistDataList(playlistId)
        viewModel.getLikes(userId)
    }

    val trackList by viewModel.trackListState.collectAsState()
    val likeList by viewModel.likeListState.collectAsState()
    TrackList(trackList = trackList, likeList = likeList, onTrackClick = {}, onLikeClick = { track, isLike ->
        if (isLike){
            viewModel.deleteLikeByTrackId(userId, track.id)
        } else {
            viewModel.insertLike(Like(0,userId,track.id,track.title,track.artist.name,track.album.cover,track.preview))
        }
    })
}

