package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cevdetkilickeser.stopify.convertStandardCharsets
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.ui.component.UserPlaylist
import com.cevdetkilickeser.stopify.viewmodel.VMUserPlayerList
import com.google.gson.Gson

@Composable
fun UserPlayerListScreen(navController: NavController, userId: String, userPlayerlistId:Int, viewModel: VMUserPlayerList = hiltViewModel()) {

    val userPlaylist by viewModel.userPlaylistState.collectAsState()

    LaunchedEffect(userId, userPlayerlistId) {
        viewModel.getUserPlaylist(userId, userPlayerlistId)
    }

    UserPlaylist(
        userPlaylist = userPlaylist,
        onClick = { userPlaylistTrack ->
            val playerTrackList = userPlaylist.map { PlayerTrack(it.trackId,it.trackTitle.convertStandardCharsets().replace("+"," "),it.trackPreview.convertStandardCharsets(),it.trackImage.convertStandardCharsets(),it.trackArtistName.convertStandardCharsets().replace("+"," ")) }
            val playerTrackListGson = Gson().toJson(playerTrackList)
            val playerTrack = playerTrackList.find { it.trackId == userPlaylistTrack.trackId }
            val startIndex = playerTrack?.let { playerTrackList.indexOf(it) } ?: 0
            navController.navigate("player/$startIndex/$playerTrackListGson")
        },
        onDeleteClick = { userPlaylistTrack ->
            viewModel.deleteUserPlaylistTrack(userPlaylistTrack)
        }
    )
}
