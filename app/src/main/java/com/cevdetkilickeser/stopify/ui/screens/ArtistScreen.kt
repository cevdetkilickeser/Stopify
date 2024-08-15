package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.model.artist.ArtistAlbumData
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.ui.component.OfflineInfo
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
    val isConnected by viewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected, artistId) {
        viewModel.getArtist(artistId)
    }

    if (!isConnected) {
        OfflineInfo(onClick = { navController.navigate("downloads")})
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
                            model = artist!!.pictureXl,
                            error = painterResource(id = R.drawable.ic_play),
                            fallback = painterResource(id = R.drawable.ic_play)
                        ),
                        contentDescription = "Artist Image",
                        modifier = Modifier
                            .size(200.dp)
                    )

                    Text(text = artist!!.name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(text = "Albums", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                    }

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
}

@Composable
fun ArtistAlbumList(albumList: List<ArtistAlbumData>, onAlbumClick: (ArtistAlbumData) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        items(albumList) { album ->
            ArtistAlbumtItem(album = album, onAlbumClick = onAlbumClick)
        }
    }
}

@Composable
fun ArtistAlbumtItem(album: ArtistAlbumData, onAlbumClick: (ArtistAlbumData) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onAlbumClick(album) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = album.coverXl,
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
                text = album.title,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}