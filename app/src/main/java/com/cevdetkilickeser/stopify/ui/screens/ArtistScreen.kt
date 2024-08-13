package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.ui.component.ArtistAlbumList
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.viewmodel.VMArtist

@Composable
fun ArtistScreen(
    navController: NavController,
    viewModel: VMArtist = hiltViewModel(),
    artistId: String
) {
    val artist by viewModel.artistState.collectAsState()
    val artistAlbums by viewModel.artistAlbumState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.getArtist(artistId)
        viewModel.getArtistArtistAlbum(artistId)
    }

    if (errorState.isNullOrEmpty()) {
        if (loadingState) {
            LoadingComponent()
        } else {
            Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(color = Color.White)) {

                Image(
                    painter = rememberAsyncImagePainter(
                        model = artist?.pictureXl,
                        error = painterResource(id = R.drawable.ic_play),
                        fallback = painterResource(id = R.drawable.ic_play)
                    ),
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
    } else {
        ErrorScreen(errorMessage = errorState!!)
    }
}