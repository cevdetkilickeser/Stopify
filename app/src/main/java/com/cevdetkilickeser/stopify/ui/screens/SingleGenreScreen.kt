package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.convertStandardCharsetsReplacePlusWithSpace
import com.cevdetkilickeser.stopify.data.model.single_genre.SingleGenreData
import com.cevdetkilickeser.stopify.isInternetAvailable
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.cevdetkilickeser.stopify.ui.component.OfflineInfo
import com.cevdetkilickeser.stopify.viewmodel.VMSingleGenre

@Composable
fun SingleGenreScreen(
    navController: NavController,
    genreId: String,
    genreName: String,
    viewModel: VMSingleGenre = hiltViewModel()
) {
    val context = LocalContext.current
    val singleGenreDataList by viewModel.state.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState(isInternetAvailable(context))
    var launchEffectInitializer by rememberSaveable { mutableStateOf(!isConnected) }

    LaunchedEffect(isConnected) {
        if (launchEffectInitializer != isConnected) {
            viewModel.getSingleGenreDataList(genreId)
            launchEffectInitializer  = isConnected
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
        Text(
            text = genreName,
            fontSize = 24.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(8.dp)
        )
        if (!isConnected) {
            OfflineInfo(onClick = { navController.navigate("downloads") })
        } else {
            if (errorState.isNullOrEmpty()) {
                if (loadingState) {
                    LoadingComponent()
                } else {
                    SingleGenreList(
                        singleGenreDataList = singleGenreDataList,
                        onClick = { singleGenreData ->
                            navController.navigate("playlist/${genreName.convertStandardCharsetsReplacePlusWithSpace()}/${singleGenreData.id}/${singleGenreData.title.convertStandardCharsetsReplacePlusWithSpace()}")
                        }
                    )
                }
            } else {
                ErrorScreen(errorMessage = errorState!!)
            }
        }
    }
}

@Composable
fun SingleGenreList(
    singleGenreDataList: List<SingleGenreData>,
    onClick: (SingleGenreData) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        items(singleGenreDataList) { singleGenre ->
            SingleGenreRow(singleGenreData = singleGenre, onClick = onClick)
        }
    }
}

@Composable
fun SingleGenreRow(singleGenreData: SingleGenreData, onClick: (SingleGenreData) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(singleGenreData) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = singleGenreData.picture,
                error = painterResource(id = R.drawable.ic_play),
                fallback = painterResource(id = R.drawable.ic_play)
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = singleGenreData.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}