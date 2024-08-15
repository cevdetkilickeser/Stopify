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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.convertStandardCharsets
import com.cevdetkilickeser.stopify.convertStandardCharsetsReplacePlusWithSpace
import com.cevdetkilickeser.stopify.data.entity.UserPlaylistTrack
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.viewmodel.VMUserPlaylist
import com.google.gson.Gson

@Composable
fun UserPlayListScreen(
    navController: NavController,
    userId: String,
    userPlaylistId: Int,
    userPlaylistName: String,
    viewModel: VMUserPlaylist = hiltViewModel()
) {

    val userPlaylist by viewModel.userPlaylistState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    LaunchedEffect(userId, userPlaylistId) {
        viewModel.getUserPlaylist(userId, userPlaylistId)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = userPlaylistName,
            fontSize = 24.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        if (errorState.isNullOrEmpty()) {
            if (loadingState) {
                LoadingComponent()
            } else {
                UserPlaylist(
                    userPlaylist = userPlaylist,
                    onClick = { userPlaylistTrack ->
                        val playerTrackList = userPlaylist.map {
                            PlayerTrack(
                                it.trackId,
                                it.trackTitle.convertStandardCharsetsReplacePlusWithSpace(),
                                it.trackPreview.convertStandardCharsets(),
                                it.trackImage.convertStandardCharsets(),
                                it.trackArtistName.convertStandardCharsetsReplacePlusWithSpace()
                            )
                        }
                        val playerTrackListGson = Gson().toJson(playerTrackList)
                        val playerTrack =
                            playerTrackList.find { it.trackId == userPlaylistTrack.trackId }
                        val startIndex = playerTrack?.let { playerTrackList.indexOf(it) } ?: 0
                        navController.navigate("player/$startIndex/$playerTrackListGson")
                    },
                    onDeleteClick = { userPlaylistTrack ->
                        viewModel.deleteUserPlaylistTrack(userPlaylistTrack)
                    }
                )
            }
        } else {
            ErrorScreen(errorMessage = errorState!!)
        }
    }
}

@Composable
fun UserPlaylist(
    userPlaylist: List<UserPlaylistTrack>,
    onClick: (UserPlaylistTrack) -> Unit,
    onDeleteClick: (UserPlaylistTrack) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        items(userPlaylist) { userPlaylistTrack ->
            UserPlaylistItem(
                userPlaylistTrack = userPlaylistTrack,
                onClick = onClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun UserPlaylistItem(
    userPlaylistTrack: UserPlaylistTrack,
    onClick: (UserPlaylistTrack) -> Unit,
    onDeleteClick: (UserPlaylistTrack) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(userPlaylistTrack) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = userPlaylistTrack.trackImage,
                    error = painterResource(id = R.drawable.ic_play),
                    fallback = painterResource(id = R.drawable.ic_play)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = userPlaylistTrack.trackTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .width(200.dp)
                )
                Text(
                    text = userPlaylistTrack.trackArtistName,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        IconButton(onClick = { onDeleteClick(userPlaylistTrack) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}
