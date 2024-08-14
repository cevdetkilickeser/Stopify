package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.convertStandardCharsetsReplacePlusWithSpace
import com.cevdetkilickeser.stopify.data.model.genre.GenreData
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.ui.component.OfflineInfo
import com.cevdetkilickeser.stopify.viewmodel.VMHome

@Composable
fun HomeScreen(navController: NavController, viewModel: VMHome = hiltViewModel()) {
    val genreDataList by viewModel.genreDataState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        viewModel.getGenreDataList()
    }

    if (!isConnected) {
        OfflineInfo(onClick = { navController.navigate("downloads")})
    } else {
        if (errorState.isNullOrEmpty()) {
            if (loadingState) {
                LoadingComponent()
            } else {
                GenreGrid(genreDataList = genreDataList, onClick = { genreData ->
                    navController.navigate("single_genre/${genreData.id}/${genreData.name.convertStandardCharsetsReplacePlusWithSpace()}")
                })
            }
        } else {
            ErrorScreen(errorMessage = errorState!!)
        }
    }
}

@Composable
fun GenreGrid(genreDataList: List<GenreData>, onClick: (GenreData) ->  Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(genreDataList) { genre ->
            GenreCard(genreData = genre, onClick)
        }
    }
}

@Composable
fun GenreCard(genreData: GenreData, onClick: (GenreData) ->  Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .padding(all = 2.dp)
            .clickable {
                onClick(genreData)
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = genreData.picture,
                    error = painterResource(id = R.drawable.ic_play),
                    fallback = painterResource(id = R.drawable.ic_play)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = genreData.name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}
