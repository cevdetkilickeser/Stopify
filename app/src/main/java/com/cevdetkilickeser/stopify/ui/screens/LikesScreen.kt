package com.cevdetkilickeser.stopify.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.viewmodel.VMLikes

@Composable
fun LikesScreen(navController: NavController, userId: String, viewModel: VMLikes = hiltViewModel()) {
    val likes by viewModel.likeListState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.getLikes(userId)
    }

    LikeList(likes = likes, onLikeClick = { like -> },
        onDeleteLikeClick = { like ->
            viewModel.deleteLike(like)
        })

}

@Composable
fun LikeList(likes: List<Like>, onLikeClick: (Like) -> Unit, onDeleteLikeClick: (Like) -> Unit) {
    LazyColumn {
        items(likes) { like ->
            LikeItem(like = like, onLikeClick = onLikeClick, onDeleteLikeClick)
        }
    }
}

@Composable
fun LikeItem(like: Like, onLikeClick: (Like) -> Unit, onDeleteLikeClick: (Like) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onLikeClick(like) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(like.trackImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = like.trackTitle,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = like.trackArtistName,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        IconButton(onClick = { onDeleteLikeClick(like) }) {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
        }
    }
}