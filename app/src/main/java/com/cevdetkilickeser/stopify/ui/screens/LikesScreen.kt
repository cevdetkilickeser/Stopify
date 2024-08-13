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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.convertStandardCharsets
import com.cevdetkilickeser.stopify.convertStandardCharsetsReplacePlusWithSpace
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
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
                val playerTrackList = likeList.map { PlayerTrack(it.trackId,it.trackTitle.convertStandardCharsetsReplacePlusWithSpace(),it.trackPreview.convertStandardCharsets(),it.trackImage.convertStandardCharsets(),it.trackArtistName.convertStandardCharsetsReplacePlusWithSpace()) }
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

@Composable
fun LikeList(likeList: List<Like>, onLikeClick: (Like) -> Unit, onDeleteLikeClick: (Like) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        items(likeList) { like ->
            LikeItem(like = like, onLikeClick = onLikeClick, onDeleteLikeClick = onDeleteLikeClick)
        }
    }
}

@Composable
fun LikeItem(like: Like, onLikeClick: (Like) -> Unit, onDeleteLikeClick: (Like) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onLikeClick(like) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = like.trackImage,
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
                    text = like.trackTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .width(200.dp)
                )
                Text(
                    text = like.trackArtistName,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        IconButton(onClick = { onDeleteLikeClick(like) }) {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
        }
    }
}

