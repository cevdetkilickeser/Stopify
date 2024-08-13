package com.cevdetkilickeser.stopify.ui.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.model.artist.ArtistAlbumData

@Composable
fun ArtistAlbumList(albumList: List<ArtistAlbumData>, onAlbumClick: (ArtistAlbumData) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .background(color = Color.White).fillMaxSize()
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
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}