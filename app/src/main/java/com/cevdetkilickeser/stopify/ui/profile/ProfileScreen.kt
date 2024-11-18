package com.cevdetkilickeser.stopify.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cevdetkilickeser.stopify.convertStandardCharsetsReplacePlusWithSpace
import com.cevdetkilickeser.stopify.data.model.playlist.UserPlaylistResponse
import com.cevdetkilickeser.stopify.ui.component.ErrorScreen
import com.cevdetkilickeser.stopify.ui.component.LoadingComponent
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    navController: NavController,
    userId: String,
    viewModel: VMProfile = hiltViewModel()
) {

    val userPlaylistResponses by viewModel.userPlaylistResponsesState.collectAsState()
    val loadingState by viewModel.loadingState.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.getUserPlaylistResponses(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp)
                    .clickable { navController.navigate("downloads") },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.LightGray
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Downloads",
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.LightGray
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Playlists",
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
                if (errorState.isNullOrEmpty()) {
                    if (loadingState) {
                        LoadingComponent()
                    } else {
                        UserPlaylistResponses(
                            userPlaylistResponses = userPlaylistResponses,
                            onClick = { userPlaylistResponse ->
                                navController.navigate("userPlaylist/${userPlaylistResponse.userPlaylistId}/${userPlaylistResponse.userPlaylistName.convertStandardCharsetsReplacePlusWithSpace()}")
                            }
                        )
                    }
                } else {
                    ErrorScreen(errorMessage = errorState!!)
                }
            }
        }
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(text = "Sign Out")
        }
    }
}

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