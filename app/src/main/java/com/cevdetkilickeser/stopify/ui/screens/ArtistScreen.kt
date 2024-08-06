package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.ui.component.ArtistAlbumList
import com.cevdetkilickeser.stopify.viewmodel.VMArtist

@Composable
fun ArtistScreen(
    navController: NavController,
    viewModel: VMArtist = hiltViewModel(),
    artistId: String
) {

    val artist by viewModel.artistState.collectAsState()
    val artistAlbums by viewModel.artistAlbumState.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.getArtist(artistId)
        viewModel.getArtistArtistAlbum(artistId)
    }

    Column (horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = rememberAsyncImagePainter(artist?.pictureXl),
            contentDescription = "Artist Image",
            modifier = Modifier
                .size(200.dp)
                .fillMaxWidth(1f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ArtistAlbumList(
            albumList = artistAlbums,
            onAlbumClick = { album ->
                navController.navigate("album/${album.id}")
            }
        )
    }
}