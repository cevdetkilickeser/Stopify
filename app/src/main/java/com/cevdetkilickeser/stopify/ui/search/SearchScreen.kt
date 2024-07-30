package com.cevdetkilickeser.stopify.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cevdetkilickeser.stopify.ui.playlist.TrackList
import com.cevdetkilickeser.stopify.viewmodel.VMSearch
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(viewModel: VMSearch = hiltViewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("track") }
    val filterOptions = listOf("track", "album", "artist")

    Column(modifier = Modifier.fillMaxSize()) {
        val searchResults by viewModel.state.collectAsState()
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
        TrackList(trackList = searchResults)
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
                DropdownMenuItem(text = { option },
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
