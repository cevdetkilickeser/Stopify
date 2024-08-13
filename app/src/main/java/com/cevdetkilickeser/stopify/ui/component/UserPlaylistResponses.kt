package com.cevdetkilickeser.stopify.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cevdetkilickeser.stopify.data.model.playlist.UserPlaylistResponse

@Composable
fun UserPlaylistResponses(userPlaylistResponses: List<UserPlaylistResponse>, onClick: (UserPlaylistResponse) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight(0.85f)
            .padding(bottom = 8.dp)
    ) {
        items(userPlaylistResponses) { userPlaylistResponse ->
            UserPlaylistItem(userPlaylistResponse = userPlaylistResponse, onClick = onClick)
        }
    }
}

@Composable
fun UserPlaylistItem(
    userPlaylistResponse: UserPlaylistResponse,
    onClick: (UserPlaylistResponse) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        onClick = { onClick(userPlaylistResponse) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(color = Color.Transparent)
    ) {
        Text(
            text = userPlaylistResponse.userPlaylistName,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(8.dp))
    }
}