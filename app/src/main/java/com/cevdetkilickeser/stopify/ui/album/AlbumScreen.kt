package com.cevdetkilickeser.stopify.ui.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.isInternetAvailable
import com.cevdetkilickeser.stopify.json
import com.cevdetkilickeser.stopify.preparePlayerTrackListForRoute
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.ui.component.OfflineInfo
import com.cevdetkilickeser.stopify.ui.component.TrackList
import kotlinx.serialization.builtins.ListSerializer

@Composable
fun AlbumScreen(
    navController: NavController,
    viewModel: VMAlbum = hiltViewModel(),
    albumId: String,
    userId: String
) {
    val context = LocalContext.current
    val album by viewModel.albumState.collectAsState()
    val playerTrackList by viewModel.playerTrackListState.collectAsState()
    val likeList by viewModel.likeListState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    var launchEffectInitializer by rememberSaveable { mutableStateOf(!isInternetAvailable(context))}

    LaunchedEffect(isConnected) {
        if (launchEffectInitializer != isConnected) {
            viewModel.getAlbum(albumId)
            viewModel.getLikes(userId)
            launchEffectInitializer  = isConnected
        }
    }

    if (!isConnected) {
        OfflineInfo(onClick = { navController.navigate("downloads") })
    } else {
        if (errorState.isNullOrEmpty()) {
            if (loadingState) {
                LoadingComponent()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.background(color = Color.White)
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = album!!.cover,
                            error = painterResource(id = R.drawable.ic_play),
                            fallback = painterResource(id = R.drawable.ic_play)
                        ),
                        contentDescription = "Album Image",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(top = 16.dp)
                    )

                    Text(text = album!!.title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(text = "Songs", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                    }
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
            }
        } else {
            ErrorScreen(errorMessage = errorState!!)
        }
    }
}