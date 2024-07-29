package com.cevdetkilickeser.stopify.ui.single_genre

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.data.playlist.SingleGenreData
import com.cevdetkilickeser.stopify.viewmodel.VMSingleGenre

@Composable
fun SingleGenreScreen(genreId: String, viewModel: VMSingleGenre = hiltViewModel()) {
    LaunchedEffect(key1 = genreId) {
        viewModel.getSingleGenreData(genreId)
    }
    val singleGenreDataList by viewModel.state.collectAsState()
    SingleGenreList(singleGenreDataList = singleGenreDataList)
}

@Composable
fun SingleGenreList(singleGenreDataList: List<SingleGenreData>) {
    LazyColumn {
        items(singleGenreDataList) { singleGenre ->
            SingleGenreRow(singleGenreData = singleGenre)
        }
    }
}

@Composable
fun SingleGenreRow(singleGenreData: SingleGenreData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {  },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(singleGenreData.picture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = singleGenreData.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp))
    }
}