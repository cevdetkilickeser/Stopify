package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.convertStandardCharsets
import com.cevdetkilickeser.stopify.convertStandardCharsetsReplacePlusWithSpace
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.album.TrackData
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.json
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.ui.component.OfflineInfo
import com.cevdetkilickeser.stopify.viewmodel.VMAlbum
import kotlinx.serialization.builtins.ListSerializer

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
    val isConnected by viewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected, albumId) {
        viewModel.getAlbum(albumId)
        viewModel.getLikes(userId)
    }

    if (!isConnected) {
        OfflineInfo(onClick = { navController.navigate("downloads") })
    } else {
        if (errorState.isNullOrEmpty()) {
            if (loadingState) {
                LoadingComponent()
            } else {
                val trackList = album!!.tracks.trackDataList
                    .filter { track -> track.preview.isNotEmpty() }

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
                    )

                    Text(text = album!!.title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(text = "Songs", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                    }

                    AlbumTrackList(
                        isSearch = false,
                        trackList = trackList,
                        likeList = likeList,
                        onTrackClick = { track ->
                            val playerTrackList = trackList.map {
                                PlayerTrack(
                                    it.id,
                                    it.title.convertStandardCharsetsReplacePlusWithSpace(),
                                    it.preview.convertStandardCharsets(),
                                    it.trackDataAlbum.coverXl.convertStandardCharsets(),
                                    it.trackDataArtist.name.convertStandardCharsetsReplacePlusWithSpace()
                                )
                            }
                            val playerTrackListJson = json.encodeToString(ListSerializer(PlayerTrack.serializer()), playerTrackList)
                            val playerTrack = playerTrackList.find { it.trackId == track.id }
                            val startIndex = playerTrack?.let { playerTrackList.indexOf(it) } ?: 0
                            navController.navigate("player/$startIndex/$playerTrackListJson")
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
}

@Composable
fun AlbumTrackList(
    isSearch: Boolean,
    trackList: List<TrackData>,
    likeList: List<Like> = emptyList(),
    onTrackClick: (TrackData) -> Unit,
    onLikeClick: (TrackData, Boolean) -> Unit = { _, _ -> }
) {
    LazyColumn(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        items(trackList) { track ->
            val isLike = likeList.any { it.trackId == track.id }
            if (track.preview.isNotEmpty()) {
                AlbumTrackItem(
                    isSearch = isSearch,
                    track = track,
                    onTrackClick = onTrackClick,
                    isLike = isLike,
                    onLikeClick = onLikeClick
                )
            }
        }
    }
}

@Composable
fun AlbumTrackItem(
    isSearch: Boolean,
    track: TrackData,
    isLike: Boolean,
    onTrackClick: (TrackData) -> Unit,
    onLikeClick: (TrackData, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onTrackClick(track) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = track.trackDataAlbum.coverXl,
                    error = painterResource(id = R.drawable.ic_play),
                    fallback = painterResource(id = R.drawable.ic_play)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .width(200.dp)
                )
                Text(
                    text = track.trackDataArtist.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        if (!isSearch) {
            IconButton(onClick = { onLikeClick(track, isLike) }) {
                Icon(
                    imageVector = if (isLike) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onLikeClick(track, isLike)
                        }
                )
            }
        }
    }
}