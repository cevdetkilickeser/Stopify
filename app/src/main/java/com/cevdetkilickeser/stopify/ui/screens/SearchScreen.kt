package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cevdetkilickeser.stopify.data.entity.History
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.ui.component.AlbumList
import com.cevdetkilickeser.stopify.ui.component.ArtistList
import com.cevdetkilickeser.stopify.ui.component.HistoryAlbumList
import com.cevdetkilickeser.stopify.ui.component.HistoryArtistList
import com.cevdetkilickeser.stopify.ui.component.HistoryTrackList
import com.cevdetkilickeser.stopify.ui.component.TrackList
import com.cevdetkilickeser.stopify.urlToString
import com.cevdetkilickeser.stopify.viewmodel.VMSearch
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavController, userId: String, viewModel: VMSearch = hiltViewModel()) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf("Track") }
    val filterOptions = listOf("Track", "Artist", "Album")

    LaunchedEffect(searchQuery, selectedFilter, viewModel) {
        search(searchQuery, selectedFilter, viewModel)
        viewModel.getHistory(selectedFilter)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)) {
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
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = Color.LightGray,
                    unfocusedLabelColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    focusedSuffixColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    selectionColors = TextSelectionColors(Color.Black, Color.Gray)
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
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
                "Track" -> HistoryTrackList(
                    historyList = historyTrackList,
                    onHistoryClick = { history ->
                        val playerTrackList = listOf(PlayerTrack(history.trackId!!, history.trackTitle!!.urlToString().replace("+"," "), history.trackPreview!!.urlToString(), history.trackImage!!.urlToString(), history.trackArtistName!!.urlToString().replace("+"," ")))
                        val playerTrackListGson = Gson().toJson(playerTrackList)
                        val playerTrack = playerTrackList.find { it.trackId == history.trackId }
                        val startIndex = playerTrack?.let { playerTrackList.indexOf(it) } ?: 0
                        navController.navigate("player/$startIndex/$playerTrackListGson")
                },
                    onDeleteHistoryClick = { history ->
                        viewModel.deleteHistory(history,selectedFilter)
                    }
                )
                "Artist" -> HistoryArtistList(historyList = historyArtistList, onHistoryClick = { history ->
                    navController.navigate("artist/${history.artistId}")
                },
                    onDeleteHistoryClick = { history ->
                    viewModel.deleteHistory(history,selectedFilter)
                    }
                )
                "Album" -> HistoryAlbumList(historyList = historyAlbumList, onHistoryClick = { history ->
                    navController.navigate("album/${history.albumId}")
                },
                    onDeleteHistoryClick = { history ->
                    viewModel.deleteHistory(history,selectedFilter)
                    }
                )
            }
        } else {
            when (selectedFilter) {
                "Track" -> TrackList(isSearch = true, trackList = searchResults, onTrackClick = { track ->
                    val isHistory = historyTrackList.any {it.trackId == track.id}
                    if (!isHistory){
                        viewModel.insertHistory(
                            History(
                                historyId = 0,
                                userId = userId,
                                trackId = track.id,
                                trackTitle = track.title,
                                trackImage = track.album.cover,
                                trackArtistName = track.artist.name,
                                trackPreview = track.preview
                            )
                        )
                    }
                    val playerTrackList = listOf(PlayerTrack(track.id, track.title.urlToString().replace("+"," "), track.preview.urlToString() ,track.album.cover.urlToString() ,track.artist.name.urlToString().replace("+"," ")))
                    val playerTrackListGson = Gson().toJson(playerTrackList)
                    val playerTrack = playerTrackList.find { it.trackId == track.id }
                    val startIndex = playerTrack.let { playerTrackList.indexOf(it) }
                    navController.navigate("player/$startIndex/$playerTrackListGson")
                })

                "Artist" -> ArtistList(artistList = searchByArtistResults, onArtistClick = { artist ->
                    val isHistory = historyArtistList.any {it.artistId == artist.id}
                    if (!isHistory){
                        viewModel.insertHistory(
                            History(
                                historyId = 0,
                                userId = userId,
                                artistId = artist.id,
                                artistName = artist.name,
                                artistImage = artist.picture
                            )
                        )
                    }
                    navController.navigate("artist/${artist.id}")
                })
                "Album" -> AlbumList(albumList = searchByAlbumResults, onAlbumClick = { album ->
                    val isHistory = historyAlbumList.any {it.albumId == album.id}
                    if (!isHistory){
                        viewModel.insertHistory(
                            History(
                                historyId = 0,
                                userId = userId,
                                albumId = album.id,
                                albumTitle = album.title,
                                albumImage = album.cover,
                                albumArtistName = album.artist.name
                            )
                        )
                    }
                    navController.navigate("album/${album.id}")
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
        Button(onClick = { expanded = true }, modifier = Modifier.width(90.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
            Text(selectedFilter)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(color = Color.LightGray)) {
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



suspend fun search(searchQuery: String, selectedFilter: String, viewModel: VMSearch) {
    if (searchQuery.isNotEmpty()) {
        delay(1000)
        when (selectedFilter) {
            "Track" -> viewModel.getSearchResponse(searchQuery)
            "Artist" -> viewModel.getSearchByArtistResponse(searchQuery)
            "Album" -> viewModel.getSearchByAlbumResponse(searchQuery)
        }
    } else {
        viewModel.clearSearchResult()
    }
}
