package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.ui.component.TrackList
import com.cevdetkilickeser.stopify.urlToString
import com.cevdetkilickeser.stopify.viewmodel.VMPlaylist
import com.google.gson.Gson

@Composable
fun PlaylistScreen(
    navController: NavController,
    playlistId: String,
    userId: String,
    viewModel: VMPlaylist = hiltViewModel()
) {

    LaunchedEffect(key1 = playlistId, key2 = userId) {
        viewModel.getPlaylistDataList(playlistId)
        viewModel.getLikes(userId)
    }

    val trackList by viewModel.trackListState.collectAsState()
    val likeList by viewModel.likeListState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    if (errorState.isNullOrEmpty()) {
        if (loadingState) {
            LoadingComponent()
        } else {
            TrackList(
                isSearch = false,
                trackList = trackList,
                likeList = likeList,
                onTrackClick = { track ->
                    val playerTrackList = trackList.map { PlayerTrack(it.id,it.title.urlToString().replace("+"," "),it.preview.urlToString(),it.album.cover.urlToString(),it.artist.name.urlToString().replace("+"," ")) }
                    println("PlayerScreen $playerTrackList")
                    val playerTrackListGson = Gson().toJson(playerTrackList)
                    val playerTrack = playerTrackList.find { it.trackId == track.id }
                    val startIndex = playerTrack?.let { playerTrackList.indexOf(it) } ?: 0
                    navController.navigate("player/$startIndex/$playerTrackListGson")
                },
                onLikeClick = { track, isLike ->
                    if (isLike) {
                        viewModel.deleteLikeByTrackId(userId, track.id)
                    } else {
                        viewModel.insertLike(
                            Like(
                                0,
                                userId,
                                track.id,
                                track.title,
                                track.artist.name,
                                track.album.cover,
                                track.preview
                            )
                        )
                    }
                }
            )
        }
    } else {
        ErrorScreen(errorMessage = errorState!!)
    }
}

