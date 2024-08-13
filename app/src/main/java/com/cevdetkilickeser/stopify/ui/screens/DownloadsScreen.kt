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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.convertStandardCharsets
import com.cevdetkilickeser.stopify.data.entity.Download
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
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

@Composable
fun DownloadList(
    downloadList: List<Download>,
    likeList: List<Like>,
    onClick: (Download) -> Unit,
    onLikeClick: (Download, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        items(downloadList) { download ->
            val isLike = likeList.any {it.trackId == download.trackId}
            DownloadItem(
                download = download,
                isLike = isLike,
                onClick = onClick,
                onLikeClick = onLikeClick
            )
        }
    }
}

@Composable
fun DownloadItem(
    download: Download,
    isLike: Boolean,
    onClick: (Download) -> Unit,
    onLikeClick: (Download, Boolean) -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(download) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = download.trackImage,
                    error = painterResource(R.drawable.ic_play),
                    fallback = painterResource(R.drawable.ic_play)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = download.trackTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .width(200.dp)
                )
                Text(
                    text = download.trackArtistName,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        IconButton(onClick = { onLikeClick(download, isLike) }) {
            Icon(
                imageVector = if (isLike) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null
            )
        }
    }
}