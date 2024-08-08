package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.cevdetkilickeser.stopify.urlToString
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

    LaunchedEffect(albumId) {
        viewModel.getAlbum(albumId)
        viewModel.getLikes(userId)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(color = Color.White)) {

        Image(
            painter = rememberAsyncImagePainter(album?.cover),
            contentDescription = "Album Image",
            modifier = Modifier
                .size(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        album?.let {
            AlbumTrackList(
                isSearch = false,
                trackList = album!!.tracks.trackDataList,
                likeList = likeList,
                onTrackClick = { track ->
                    val playerTrack = PlayerTrack(
                        track.id.urlToString(),
                        track.title.urlToString().replace("+"," "),
                        track.preview.urlToString(),
                        track.trackDataAlbum.cover.urlToString(),
                        track.trackDataArtist.name.urlToString().replace("+"," ")
                    )
                    val playerTrackGson = Gson().toJson(playerTrack)
                    navController.navigate("player/$playerTrackGson")
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
                                track.trackDataAlbum.cover,
                                track.preview
                            )
                        )
                    }
                }
            )
        }
    }
}