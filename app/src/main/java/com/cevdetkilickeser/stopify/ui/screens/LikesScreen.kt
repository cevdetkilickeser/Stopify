package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.ui.component.LikeList
import com.cevdetkilickeser.stopify.urlToString
import com.cevdetkilickeser.stopify.viewmodel.VMLikes
import com.google.gson.Gson

@Composable
fun LikesScreen(
    navController: NavController,
    userId: String,
    viewModel: VMLikes = hiltViewModel()
) {
    val likeList by viewModel.likeListState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.getLikes(userId)
    }

    LikeList(likeList = likeList, onLikeClick = { like ->
        val playerTrack = PlayerTrack(
            like.trackId.urlToString(),
            like.trackTitle.urlToString().replace("+"," "),
            like.trackPreview.urlToString(),
            like.trackImage.urlToString(),
            like.trackArtistName.urlToString().replace("+"," ")
        )
        val playerTrackGson = Gson().toJson(playerTrack)
        navController.navigate("player/$playerTrackGson")
    },
        onDeleteLikeClick = { like ->
            viewModel.deleteLike(like)
        }
    )
}

