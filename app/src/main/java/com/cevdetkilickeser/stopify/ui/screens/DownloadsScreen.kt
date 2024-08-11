package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cevdetkilickeser.stopify.convertStandardCharsets
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.ui.component.DownloadList
import com.cevdetkilickeser.stopify.viewmodel.VMDownloads
import com.google.gson.Gson

@Composable
fun DownloadsScreen(
    navController: NavController,
    userId: String,
    viewModel: VMDownloads = hiltViewModel()
) {

    LaunchedEffect(userId) {
        viewModel.getDownloads()
        viewModel.getLikes(userId)
    }

    val downloadList by viewModel.downloadListState.collectAsState()
    val likeList by viewModel.likeListState.collectAsState()

    DownloadList(
        downloadList = downloadList,
        likeList = likeList,
        onClick = { download ->
            val playerTrackList = downloadList.map { PlayerTrack(it.trackId,it.trackTitle.convertStandardCharsets().replace("+"," "),it.fileUri!!.convertStandardCharsets(), it.trackImage.convertStandardCharsets(),it.trackArtistName.convertStandardCharsets().replace("+"," ")) }
            val playerTrackListGson = Gson().toJson(playerTrackList)
            val playerTrack = playerTrackList.find { it.trackId == download.trackId }
            val startIndex = playerTrack?.let { playerTrackList.indexOf(it) } ?: 0
            navController.navigate("player/$startIndex/$playerTrackListGson")
        },
        onLikeClick = { download, isLike ->
            if (isLike) {
                viewModel.deleteLikeByTrackId(userId, download.trackId)
            } else {
                viewModel.insertLike(
                    Like(
                        0,
                        userId,
                        download.trackId,
                        download.trackTitle,
                        download.trackArtistName,
                        download.trackImage,
                        download.trackPreview
                    )
                )
            }
        }
    )
}