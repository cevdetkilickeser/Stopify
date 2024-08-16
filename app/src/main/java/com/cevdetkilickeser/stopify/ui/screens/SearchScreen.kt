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
import com.cevdetkilickeser.stopify.json
import com.cevdetkilickeser.stopify.preparePlayerTrackListForRoute
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.ui.component.OfflineInfo
import com.cevdetkilickeser.stopify.ui.component.TrackList
import com.cevdetkilickeser.stopify.ui.component.AlbumList
import com.cevdetkilickeser.stopify.ui.component.ArtistList
import com.cevdetkilickeser.stopify.viewmodel.VMSearch
import kotlinx.serialization.builtins.ListSerializer

@Composable
fun SearchScreen(
    navController: NavController,
    userId: String,
    viewModel: VMSearch = hiltViewModel()
) {

    val playerTrackList by viewModel.playerTrackListState.collectAsState()
    val searchByArtistResults by viewModel.artistListState.collectAsState()
    val searchByAlbumResults by viewModel.albumListState.collectAsState()
    val historyPlayerTrackList by viewModel.historyPlayerTrackListState.collectAsState()
    val historyArtistList by viewModel.historyArtistListState.collectAsState()
    val historyAlbumList by viewModel.historyAlbumListState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf("Track") }
    val filterOptions = listOf("Track", "Artist", "Album")

    LaunchedEffect(isConnected, searchQuery, selectedFilter) {
        viewModel.search(searchQuery, selectedFilter)
        viewModel.getHistory(selectedFilter)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
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
                    viewModel.getHistory(selectedFilter)
                    viewModel.search(searchQuery, selectedFilter)
                },
                filterOptions = filterOptions
            )
        }

        if (searchQuery.isEmpty()) {
            when (selectedFilter) {
                "Track" -> TrackList(
                    likeIcon = false,
                    deleteIcon = true,
                    playerTrackList = historyPlayerTrackList,
                    likeList = emptyList(),
                    onTrackClick = { track ->
                        val historyTrack = listOf(track)
                        val playerTrackListJson = json.encodeToString(
                            ListSerializer(PlayerTrack.serializer()),
                            historyTrack.preparePlayerTrackListForRoute()
                        )
                        val startIndex = 0
                        navController.navigate("player/$startIndex/$playerTrackListJson")
                    },
                    onDeleteClick = { track ->
                        viewModel.deleteHistory(
                            History(
                                track.historyId!!,
                                userId
                            ),
                            selectedFilter
                        )
                    }
                )

                "Artist" -> ArtistList(
                    deleteIcon = true,
                    artistList = historyArtistList,
                    onClick = { artist ->
                        navController.navigate("artist/${artist.id}")
                    },
                    onDeleteClick = { artist ->
                        viewModel.deleteHistory(
                            History(
                                artist.historyId!!,
                                userId
                            ),
                            selectedFilter
                        )
                    }
                )

                "Album" -> AlbumList(
                    deleteIcon = true,
                    albumList = historyAlbumList,
                    onClick = { album ->
                        navController.navigate("album/${album.id}")
                    },
                    onDeleteClick = { album ->
                        viewModel.deleteHistory(
                            History(
                                album.historyId!!,
                                userId
                            ),
                            selectedFilter
                        )
                    }
                )
            }
        } else {
            if (!isConnected) {
                OfflineInfo(onClick = { navController.navigate("downloads") })
            } else {
                if (errorState.isNullOrEmpty()) {
                    if (loadingState) {
                        LoadingComponent()
                    } else {
                        when (selectedFilter) {
                            "Track" -> TrackList(
                                likeIcon = true,
                                deleteIcon = false,
                                playerTrackList = playerTrackList,
                                likeList = emptyList(),
                                onTrackClick = { track ->
                                    val searchedTrack = listOf(track)
                                    val playerTrackListJson = json.encodeToString(
                                        ListSerializer(PlayerTrack.serializer()),
                                        searchedTrack.preparePlayerTrackListForRoute()
                                    )
                                    val startIndex = 0
                                    navController.navigate("player/$startIndex/$playerTrackListJson")
                                    val isHistory =
                                        historyPlayerTrackList.any { it.trackId == track.trackId }
                                    if (!isHistory) {
                                        viewModel.insertHistory(
                                            History(
                                                historyId = 0,
                                                userId = userId,
                                                trackId = track.trackId,
                                                trackTitle = track.trackTitle,
                                                trackImage = track.trackImage,
                                                trackArtistName = track.trackArtistName,
                                                trackPreview = track.trackPreview
                                            )
                                        )
                                    }
                                }
                            )

                            "Artist" -> ArtistList(
                                deleteIcon = false,
                                artistList = searchByArtistResults,
                                onClick = { artist ->
                                    val isHistory =
                                        historyArtistList.any { it.id == artist.id }
                                    if (!isHistory) {
                                        viewModel.insertHistory(
                                            History(
                                                historyId = 0,
                                                userId = userId,
                                                artistId = artist.id,
                                                artistName = artist.name,
                                                artistImage = artist.image
                                            )
                                        )
                                    }
                                    navController.navigate("artist/${artist.id}")
                                }
                            )

                            "Album" -> AlbumList(
                                deleteIcon = false,
                                albumList = searchByAlbumResults,
                                onClick = { album ->
                                    val isHistory = historyAlbumList.any { it.id == album.id }
                                    if (!isHistory) {
                                        viewModel.insertHistory(
                                            History(
                                                historyId = 0,
                                                userId = userId,
                                                albumId = album.id,
                                                albumTitle = album.title,
                                                albumImage = album.image,
                                                albumArtistName = album.artist
                                            )
                                        )
                                    }
                                    navController.navigate("album/${album.id}")
                                }
                            )
                        }
                    }
                } else {
                    ErrorScreen(errorMessage = errorState!!)
                }
            }
        }
    }
}

@Composable
fun QueryFilter(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    filterOptions: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.width(90.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(selectedFilter)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(color = Color.LightGray)
        ) {
            filterOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onFilterSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
