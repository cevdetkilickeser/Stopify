package com.cevdetkilickeser.stopify.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.data.playlist.Track
import com.cevdetkilickeser.stopify.data.search.AlbumData
import com.cevdetkilickeser.stopify.data.search.ArtistData
import com.cevdetkilickeser.stopify.viewmodel.VMSearch
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(viewModel: VMSearch = hiltViewModel()) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("track") }
    val filterOptions = listOf("track", "album", "artist")

    Column(modifier = Modifier.fillMaxSize()) {
        val searchResults by viewModel.trackListState.collectAsState()
        val searchByArtistResults by viewModel.artistListState.collectAsState()
        val searchByAlbumResults by viewModel.albumListState.collectAsState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            QueryFilter(
                selectedFilter = selectedFilter,
                onFilterSelected = { option ->
                    selectedFilter = option
                },
                filterOptions = filterOptions,
                searchQuery = searchQuery,
                viewModel = viewModel
            )
        }

        LaunchedEffect(searchQuery, selectedFilter, viewModel) {
            search(searchQuery, selectedFilter, viewModel)
        }
        when (selectedFilter) {
            "track" -> TrackList(trackList = searchResults)
            "artist" -> ArtistList(artistList = searchByArtistResults)
            "album" -> AlbumList(albumList = searchByAlbumResults)
        }
    }
}

@Composable
fun QueryFilter(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    filterOptions: List<String>,
    searchQuery: String,
    viewModel: VMSearch
) {
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box {
        Button(onClick = { expanded = true }) {
            Text(selectedFilter)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filterOptions.forEach { option ->
                DropdownMenuItem(text = { Text(text = option) },
                    onClick = {
                        onFilterSelected(option)
                        expanded = false
                        coroutineScope.launch {
                            search(searchQuery, selectedFilter, viewModel)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TrackList(trackList: List<Track>) {
    LazyColumn {
        items(trackList) { track ->
            TrackItem(track = track)
        }
    }
}

@Composable
fun ArtistList(artistList: List<ArtistData>) {
    LazyColumn {
        items(artistList) { artist ->
            ArtistItem(artist = artist)
        }
    }
}

@Composable
fun AlbumList(albumList: List<AlbumData>) {
    LazyColumn {
        items(albumList) { album ->
            AlbumtItem(album = album)
        }
    }
}

@Composable
fun TrackItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(track.artist.picture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = track.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = track.artist.name,
                style = MaterialTheme.typography
                    .titleSmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ArtistItem(artist: ArtistData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(artist.picture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = artist.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun AlbumtItem(album: AlbumData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(album.cover),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = album.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = album.artist.name,
                style = MaterialTheme.typography
                    .titleSmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

suspend fun search(searchQuery: String, selectedFilter: String, viewModel: VMSearch) {
    if (searchQuery.isNotEmpty()) {
        delay(1000)
        when (selectedFilter) {
            "track" -> viewModel.getSearchResponse(searchQuery)
            "album" -> viewModel.getSearchByAlbumResponse(searchQuery)
            "artist" -> viewModel.getSearchByArtistResponse(searchQuery)
        }
    } else {
        viewModel.clearSearchResult()
    }
}
