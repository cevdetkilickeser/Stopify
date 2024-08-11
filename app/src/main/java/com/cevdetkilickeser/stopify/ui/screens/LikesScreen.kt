package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LikeList
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.convertStandardCharsets
import com.cevdetkilickeser.stopify.viewmodel.VMLikes
import com.google.gson.Gson

@Composable
fun LikesScreen(
    navController: NavController,
    userId: String,
    viewModel: VMLikes = hiltViewModel()
) {
    val likeList by viewModel.likeListState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.getLikes(userId)
    }

    if (errorState.isNullOrEmpty()) {
        if (loadingState) {
            LoadingComponent()
        } else {
            LikeList(likeList = likeList, onLikeClick = { like ->
                val playerTrackList = likeList.map { PlayerTrack(it.trackId,it.trackTitle.convertStandardCharsets().replace("+"," "),it.trackPreview.convertStandardCharsets(),it.trackImage.convertStandardCharsets(),it.trackArtistName.convertStandardCharsets().replace("+"," ")) }
                val playerTrackListGson = Gson().toJson(playerTrackList)
                val playerTrack = playerTrackList.find { it.trackId == like.trackId }
                val startIndex = playerTrack?.let { playerTrackList.indexOf(it) } ?: 0
                navController.navigate("player/$startIndex/$playerTrackListGson")
            },
                onDeleteLikeClick = { like ->
                    viewModel.deleteLike(like)
                }
            )
        }
    } else {
        ErrorScreen(errorMessage = errorState!!)
    }
}

