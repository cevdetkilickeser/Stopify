package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.isInternetAvailable
import com.cevdetkilickeser.stopify.json
import com.cevdetkilickeser.stopify.preparePlayerTrackListForRoute
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.ui.component.OfflineInfo
import com.cevdetkilickeser.stopify.ui.component.TrackList
import com.cevdetkilickeser.stopify.viewmodel.VMPlaylist
import kotlinx.serialization.builtins.ListSerializer

@Composable
fun PlaylistScreen(
    navController: NavController,
    playlistId: String,
    genreName: String,
    playlistName: String,
    userId: String,
    viewModel: VMPlaylist = hiltViewModel()
) {
    val context = LocalContext.current
    val playerTrackList by viewModel.playerTrackListState.collectAsState()
    val likeList by viewModel.likeListState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    var launchEffectInitializer by rememberSaveable { mutableStateOf(!isInternetAvailable(context))}

    LaunchedEffect(isConnected) {
        if (launchEffectInitializer != isConnected) {
            viewModel.getPlaylistDataList(playlistId)
            viewModel.getLikes(userId)
            launchEffectInitializer  = isConnected
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "$genreName > $playlistName",
            fontSize = 24.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        if (!isConnected) {
            OfflineInfo(onClick = { navController.navigate("downloads") })
        } else {
            if (errorState.isNullOrEmpty()) {
                if (loadingState) {
                    LoadingComponent()
                } else {
                    TrackList(
                        likeIcon = true,
                        deleteIcon = false,
                        playerTrackList = playerTrackList,
                        likeList = likeList,
                        onTrackClick = { track ->
                            val playerTrackListJson = json.encodeToString(ListSerializer(PlayerTrack.serializer()), playerTrackList.preparePlayerTrackListForRoute())
                            val playerTrack = playerTrackList.find { it.trackId == track.trackId }
                            val startIndex = playerTrack?.let { playerTrackList.indexOf(it) } ?: 0
                            navController.navigate("player/$startIndex/$playerTrackListJson")
                        },
                        onLikeClick = { track, isLike ->
                            if (isLike) {
                                viewModel.deleteLikeByTrackId(userId, track.trackId)
                            } else {
                                viewModel.insertLike(
                                    Like(
                                        0,
                                        userId,
                                        track.trackId,
                                        track.trackTitle,
                                        track.trackArtistName,
                                        track.trackImage,
                                        track.trackPreview
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
    }
}
