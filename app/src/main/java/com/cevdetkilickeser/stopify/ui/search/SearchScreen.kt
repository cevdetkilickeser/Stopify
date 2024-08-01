package com.cevdetkilickeser.stopify.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.cevdetkilickeser.stopify.data.entity.History
import com.cevdetkilickeser.stopify.data.playlist.Track
import com.cevdetkilickeser.stopify.data.search.AlbumData
import com.cevdetkilickeser.stopify.data.search.ArtistData
import com.cevdetkilickeser.stopify.viewmodel.VMSearch
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(viewModel: VMSearch = hiltViewModel(), auth: FirebaseAuth = FirebaseAuth.getInstance()) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("track") }
    val filterOptions = listOf("track", "artist", "album")

    LaunchedEffect(searchQuery, selectedFilter, viewModel) {
        search(searchQuery, selectedFilter, viewModel)
        viewModel.getHistory(selectedFilter)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val searchResults by viewModel.trackListState.collectAsState()
        val searchByArtistResults by viewModel.artistListState.collectAsState()
        val searchByAlbumResults by viewModel.albumListState.collectAsState()
        val historyTrackList by viewModel.historyTrackListState.collectAsState()
        val historyArtistList by viewModel.historyArtistListState.collectAsState()
        val historyAlbumList by viewModel.historyAlbumListState.collectAsState()

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

        if (searchQuery.isEmpty()) {
            when (selectedFilter) {
                "track" -> HistoryTrackList(historyList = historyTrackList, onHistoryClick = { history ->

                    }, onDeleteHistoryClick = { history ->
                        viewModel.deleteHistory(history,selectedFilter)
                    }
                )
                "artist" -> HistoryArtistList(historyList = historyArtistList, onHistoryClick = { history ->

                    }, onDeleteHistoryClick = { history ->
                    viewModel.deleteHistory(history,selectedFilter)
                    }
                )
                "album" -> HistoryAlbumList(historyList = historyAlbumList, onHistoryClick = { history ->

                    }, onDeleteHistoryClick = { history ->
                    viewModel.deleteHistory(history,selectedFilter)
                    }
                )
            }
        } else {
            when (selectedFilter) {
                "track" -> TrackList(trackList = searchResults, onTrackClick = { track ->
                    viewModel.insertHistory(
                        History(
                            0,
                            auth.currentUser!!.toString(),
                            track.title,
                            track.artist.picture,
                            track.artist.name,
                            track.link
                        )
                    )
                })
                "artist" -> ArtistList(artistList = searchByArtistResults, onArtistClick = { artist ->
                        viewModel.insertHistory(
                            History(
                                0,
                                auth.currentUser!!.toString(),
                                null,
                                null,
                                null,
                                null,
                                artist.id,
                                artist.name,
                                artist.picture
                            )
                        )
                    })
                "album" -> AlbumList(albumList = searchByAlbumResults, onAlbumClick = { album ->
                    viewModel.insertHistory(
                        History(
                            0,
                            auth.currentUser!!.toString(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            album.id,
                            album.title,
                            album.cover,
                            album.artist.name
                        )
                    )
                })
            }
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
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            filterOptions.forEach { option ->
                DropdownMenuItem(text = { Text(text = option) }, onClick = {
                    onFilterSelected(option)
                    expanded = false
                    coroutineScope.launch {
                        viewModel.getHistory(selectedFilter)
                        search(searchQuery, selectedFilter, viewModel)
                    }
                })
            }
        }
    }
}

@Composable
fun TrackList(trackList: List<Track>, onTrackClick: (Track) -> Unit) {
    LazyColumn {
        items(trackList) { track ->
            TrackItem(track = track, onTrackClick = onTrackClick)
        }
    }
}

@Composable
fun ArtistList(artistList: List<ArtistData>, onArtistClick: (ArtistData) -> Unit) {
    LazyColumn {
        items(artistList) { artist ->
            ArtistItem(artist = artist, onArtistClick = onArtistClick)
        }
    }
}

@Composable
fun AlbumList(albumList: List<AlbumData>, onAlbumClick: (AlbumData) -> Unit) {
    LazyColumn {
        items(albumList) { album ->
            AlbumtItem(album = album, onAlbumClick = onAlbumClick)
        }
    }
}

@Composable
fun HistoryTrackList(
    historyList: List<History>,
    onHistoryClick: (History) -> Unit,
    onDeleteHistoryClick: (History) -> Unit
) {
    LazyColumn {
        items(historyList) { history ->
            HistoryTrackItem(history = history, onHistoryClick = onHistoryClick, onDeleteHistoryClick = onDeleteHistoryClick)
        }
    }
}

@Composable
fun HistoryArtistList(
    historyList: List<History>,
    onHistoryClick: (History) -> Unit,
    onDeleteHistoryClick: (History) -> Unit
) {
    LazyColumn {
        items(historyList) { history ->
            HistoryArtistItem(history = history, onHistoryClick = onHistoryClick, onDeleteHistoryClick = onDeleteHistoryClick)
        }
    }
}

@Composable
fun HistoryAlbumList(
    historyList: List<History>,
    onHistoryClick: (History) -> Unit,
    onDeleteHistoryClick: (History) -> Unit
) {
    LazyColumn {
        items(historyList) { history ->
            HistoryAlbumItem(history = history, onHistoryClick = onHistoryClick, onDeleteHistoryClick = onDeleteHistoryClick)
        }
    }
}

@Composable
fun TrackItem(track: Track, onTrackClick: (Track) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onTrackClick(track) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(track.album.cover),
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
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ArtistItem(artist: ArtistData, onArtistClick: (ArtistData) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onArtistClick(artist) },
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
fun AlbumtItem(album: AlbumData, onAlbumClick: (AlbumData) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onAlbumClick(album) },
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
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun HistoryTrackItem(
    history: History,
    onHistoryClick: (History) -> Unit,
    onDeleteHistoryClick: (History) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onHistoryClick(history) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(history.trackImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = history.trackTitle!!,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = history.trackArtistName!!,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        IconButton(onClick = { onDeleteHistoryClick(history) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}

@Composable
fun HistoryArtistItem(
    history: History,
    onHistoryClick: (History) -> Unit,
    onDeleteHistoryClick: (History) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onHistoryClick(history) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(history.artistImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = history.artistName!!,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        IconButton(onClick = { onDeleteHistoryClick(history) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}

@Composable
fun HistoryAlbumItem(
    history: History,
    onHistoryClick: (History) -> Unit,
    onDeleteHistoryClick: (History) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onHistoryClick(history) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(history.albumImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = history.albumTitle!!,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = history.albumArtistName!!,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        IconButton(onClick = { onDeleteHistoryClick(history) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}

suspend fun search(searchQuery: String, selectedFilter: String, viewModel: VMSearch) {
    if (searchQuery.isNotEmpty()) {
        delay(1000)
        when (selectedFilter) {
            "track" -> viewModel.getSearchResponse(searchQuery)
            "artist" -> viewModel.getSearchByArtistResponse(searchQuery)
            "album" -> viewModel.getSearchByAlbumResponse(searchQuery)
        }
    } else {
        viewModel.clearSearchResult()
    }
}
