package com.cevdetkilickeser.stopify.ui.component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack

@Composable
fun TrackList(
    likeIcon: Boolean,
    deleteIcon:Boolean,
    playerTrackList: List<PlayerTrack>,
    likeList: List<Like>,
    onTrackClick: (PlayerTrack) -> Unit,
    onLikeClick: (PlayerTrack, Boolean) -> Unit = { _, _ -> },
    onDeleteClick: (PlayerTrack) -> Unit = { _ -> }
) {
    LazyColumn(
        modifier = Modifier
            .background(color = Color.White)
    ) {
        items(playerTrackList) { track ->
            val isLike = likeList.any { it.trackId == track.trackId }
            TrackItem(
                likeIcon = likeIcon,
                deleteIcon = deleteIcon,
                track = track,
                isLike = isLike,
                onTrackClick = onTrackClick,
                onLikeClick = onLikeClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun TrackItem(
    likeIcon: Boolean,
    deleteIcon: Boolean,
    track: PlayerTrack,
    isLike: Boolean,
    onTrackClick: (PlayerTrack) -> Unit,
    onLikeClick: (PlayerTrack, Boolean) -> Unit,
    onDeleteClick: (PlayerTrack) -> Unit
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
                painter = rememberAsyncImagePainter(
                    model = track.trackImage,
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
                    text = track.trackTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .width(200.dp)
                )
                Text(
                    text = track.trackArtistName,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        if (likeIcon){
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
        if (deleteIcon){
            IconButton(onClick = { onDeleteClick(track) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onDeleteClick(track)
                        }
                )
            }
        }
    }
}