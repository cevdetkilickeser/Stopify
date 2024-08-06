package com.cevdetkilickeser.stopify.ui.component

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.data.album.TrackData
import com.cevdetkilickeser.stopify.data.entity.Like

@Composable
fun AlbumTrackList(
    isSearch: Boolean,
    trackList: List<TrackData>,
    likeList: List<Like> = emptyList(),
    onTrackClick: (TrackData) -> Unit,
    onLikeClick: (TrackData, Boolean) -> Unit = { _, _ -> }
) {
    LazyColumn {
        items(trackList) { track ->
            val isLike = likeList.any { it.trackId == track.id }
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
                painter = rememberAsyncImagePainter(track.trackDataAlbum.coverXl),
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
                    modifier = Modifier.padding(horizontal = 8.dp).width(200.dp)
                )
                Text(
                    text = track.trackDataArtist.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        if (!isSearch){
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