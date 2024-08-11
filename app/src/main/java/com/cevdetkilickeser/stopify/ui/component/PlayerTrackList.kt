package com.cevdetkilickeser.stopify.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack

@Composable
fun PlayerTrackList(
    playerList: List<PlayerTrack>,
    onClick: (PlayerTrack) -> Unit,
    currentTrackId: String
) {
    Column(
        modifier = Modifier
            .background(color = Color.White)) {
        Text(
            text = "Current Playlist",
            style = MaterialTheme.typography.labelLarge,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(color = Color.White)
        )
        LazyColumn(
            modifier = Modifier
                .background(color = Color.White)
        ) {
            items(playerList) { playerTrack ->
                PlayerTrackItem(
                    playerTrack = playerTrack,
                    onClick = onClick,
                    isCurrentTrack = playerTrack.trackId == currentTrackId
                )
            }
        }
    }
}

@Composable
fun PlayerTrackItem(
    playerTrack: PlayerTrack,
    onClick: (PlayerTrack) -> Unit,
    isCurrentTrack: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(playerTrack) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(playerTrack.trackImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(64.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = playerTrack.trackTitle,
                color = if (isCurrentTrack) {Color.Green} else {Color.Black},
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(200.dp)
            )
            Text(
                text = playerTrack.trackArtistName,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}