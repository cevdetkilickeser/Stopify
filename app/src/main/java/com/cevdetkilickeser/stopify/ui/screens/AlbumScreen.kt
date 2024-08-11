package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.ui.component.AlbumTrackList
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.convertStandardCharsets
import com.cevdetkilickeser.stopify.viewmodel.VMAlbum
import com.google.gson.Gson

@Composable
fun AlbumScreen(
    navController: NavController,
    viewModel: VMAlbum = hiltViewModel(),
    albumId: String,
    userId: String
) {

    val album by viewModel.albumState.collectAsState()
    val likeList by viewModel.likeListState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    LaunchedEffect(albumId) {
        viewModel.getAlbum(albumId)
        viewModel.getLikes(userId)
    }

    if (errorState.isNullOrEmpty()) {
        if (loadingState) {
            LoadingComponent()
        } else {
            val trackList = album!!.tracks.trackDataList
                .filter { track -> track.preview.isNotEmpty() }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(color = Color.White)) {

                Image(
                    painter = rememberAsyncImagePainter(album?.cover),
                    contentDescription = "Album Image",
                    modifier = Modifier
                        .size(200.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                AlbumTrackList(
                    isSearch = false,
                    trackList = trackList,
                    likeList = likeList,
                    onTrackClick = { track ->
                        val playerTrackList = trackList.map { PlayerTrack(it.id,it.title.convertStandardCharsets().replace("+"," "),it.preview.convertStandardCharsets(),it.trackDataAlbum.coverXl.convertStandardCharsets(),it.trackDataArtist.name.convertStandardCharsets().replace("+"," ")) }
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
                                    track.trackDataArtist.name,
                                    track.trackDataAlbum.coverXl,
                                    track.preview
                                )
                            )
                        }
                    }
                )
            }
        }
    } else {
        ErrorScreen(errorMessage = errorState!!)
    }
}