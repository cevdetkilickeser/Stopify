package com.cevdetkilickeser.stopify.ui.component.search_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.model.album.Album


@Composable
fun AlbumList(
    deleteIcon: Boolean,
    albumList: List<Album>,
    onClick: (Album) -> Unit,
    onDeleteClick: (Album) -> Unit = { _ -> }
) {
    LazyColumn(
        modifier = Modifier
            .background(color = Color.White)
    ) {
        items(albumList) { album ->
            AlbumtItem(
                deleteIcon = deleteIcon,
                album = album,
                onClick = onClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun AlbumtItem(
    deleteIcon: Boolean,
    album: Album,
    onClick: (Album) -> Unit,
    onDeleteClick: (Album) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(album) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = album.image,
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
                    maxLines = 2,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp).width(200.dp)
                )
                Text(
                    text = album.artist,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        if (deleteIcon) {
            IconButton(onClick = { onDeleteClick(album) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}