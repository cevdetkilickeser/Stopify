package com.cevdetkilickeser.stopify.ui.screens

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import coil.compose.rememberAsyncImagePainter
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.entity.UserPlaylistTrack
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.data.model.playlist.UserPlaylistResponse
import com.cevdetkilickeser.stopify.viewmodel.VMMusicPlayer


@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    startIndex: Int,
    playerTrackList: List<PlayerTrack>,
    userId: String,
    viewModel: VMMusicPlayer = hiltViewModel()
) {
    val context = LocalContext.current
    val isConnected by viewModel.isConnected.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val isDownload by viewModel.isDownloadState.collectAsState()
    val currentTrack by viewModel.currentTrack.collectAsState()
    val userPlaylistResponses by viewModel.userPlaylistResponsesState.collectAsState()
    val nextUserPlaylistId by viewModel.nextUserPlaylistId.collectAsState()
    val downloadInfo by viewModel.isDownloadInfoState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var newPlaylistName by remember { mutableStateOf("") }
    var addedTrack by remember { mutableStateOf(currentTrack) }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val sliderPosition = (currentPosition / 1000).toFloat()
    val sliderDuration = (duration / 1000).toFloat()

    LaunchedEffect(playerTrackList) {
        if (currentTrack == null) {
            viewModel.getDownloadList(userId, playerTrackList[startIndex].trackId)
            viewModel.getUserPlaylistResponses(userId)
            viewModel.load(startIndex, playerTrackList)
            viewModel.play()
        }
    }

    currentTrack?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            PlayerImage(it)

            Spacer(modifier = Modifier.height(24.dp))

            PlayerConsole(
                onDownloadClick = {
                    if (!isDownload) {
                        viewModel.downloadSong(it, userId)
                    } else {
                        val download = viewModel.getSingleDownload(userId, it.trackId)
                        viewModel.deleteDownload(
                            userId,
                            it.trackId,
                            download.downloadId,
                            download.fileUri,
                            context
                        )
                    }
                },
                isDownload = isDownload,
                isConnected = isConnected,
                downloadInfo = downloadInfo,
                onAddClick = {
                    expanded = true
                    addedTrack = currentTrack
                },
                expanded = expanded,
                onDropDownMenuDismissRequestClick = { expanded = false },
                userPlaylistResponses = userPlaylistResponses,
                onAddNewClick = {
                    showDialog = true
                    expanded = false
                },
                onMenuItemClick = { userPlaylistResponse ->
                    val userPlaylistTrack = UserPlaylistTrack(
                        0,
                        userId,
                        userPlaylistResponse.userPlaylistId,
                        userPlaylistResponse.userPlaylistName,
                        addedTrack!!.trackId,
                        addedTrack!!.trackPreview,
                        addedTrack!!.trackTitle,
                        addedTrack!!.trackImage,
                        addedTrack!!.trackArtistName
                    )
                    viewModel.insertTrackToUserPlaylist(userPlaylistTrack)
                    expanded = false
                },
                showDialog = showDialog,
                newPlaylistName = newPlaylistName,
                onAlertDialogDismissRequest = {
                    showDialog = false
                    expanded = true
                },
                onConfirmClick = {
                    val userPlaylistTrack = UserPlaylistTrack(
                        0,
                        userId,
                        nextUserPlaylistId,
                        newPlaylistName,
                        addedTrack!!.trackId,
                        addedTrack!!.trackPreview,
                        addedTrack!!.trackTitle,
                        addedTrack!!.trackImage,
                        addedTrack!!.trackArtistName
                    )
                    viewModel.insertTrackToNewUserPlaylist(userPlaylistTrack)
                    showDialog = false
                    newPlaylistName = ""
                },
                onDismissClick = {
                    showDialog = false
                    expanded = true
                    newPlaylistName = ""
                },
                onAlertTextValueChange = { newPlaylistName = it },
                sliderPosition = sliderPosition,
                sliderDuration = sliderDuration,
                onSliderValueChange = { newValue -> viewModel.seekTo(newValue.toLong() * 1000) },
                currentPosition = currentPosition,
                duration = duration,
                isPlaying = isPlaying,
                onPreviousClick = { viewModel.previousSong() },
                onRewindClick = { viewModel.rewind() },
                onPlayPauseClick = { if (isPlaying) viewModel.pause() else viewModel.play() },
                onFastForwardClick = { viewModel.fastForward() },
                onNextClick = { viewModel.nextSong() },
                onShareClick = {
                    val shareLink = currentTrack!!.trackPreview
                    viewModel.share(shareLink, context)
                }, onPlaylistClick = {
                    showBottomSheet = true
                }
            )
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.Gray,
                modifier = Modifier.padding(top = 64.dp)
            ) {
                var currentTrackId by remember {
                    mutableStateOf(currentTrack!!.trackId)
                }
                PlayerTrackList(
                    playerList = playerTrackList, onClick = { playerTrack ->
                        if (playerTrack.trackId == currentTrack!!.trackId) {
                            if (isPlaying) {
                                viewModel.pause()
                            } else {
                                viewModel.play()
                            }
                        } else {
                            viewModel.seekTo(playerTrackList.indexOf(playerTrack), 0L)
                        }
                        currentTrackId = playerTrack.trackId
                    },
                    currentTrackId = currentTrackId
                )
            }
        }
    }
}

@Composable
fun PlayerImage(currentTrack: PlayerTrack) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = currentTrack.trackImage,
                error = painterResource(id = R.drawable.ic_play),
                fallback = painterResource(id = R.drawable.ic_play)
            ), contentDescription = "Track Image", modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = currentTrack.trackTitle,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = currentTrack.trackArtistName,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun PlayerConsole(
    isDownload: Boolean,
    onDownloadClick: () -> Unit,
    isConnected: Boolean,
    downloadInfo: String,
    onAddClick: () -> Unit,
    expanded: Boolean,
    onDropDownMenuDismissRequestClick: () -> Unit,
    userPlaylistResponses: List<UserPlaylistResponse>,
    onAddNewClick: () -> Unit,
    onMenuItemClick: (UserPlaylistResponse) -> Unit,
    showDialog: Boolean,
    newPlaylistName: String,
    onAlertDialogDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    onAlertTextValueChange: (String) -> Unit,
    sliderPosition: Float,
    sliderDuration: Float,
    onSliderValueChange: (Float) -> Unit,
    currentPosition: Long,
    duration: Long,
    isPlaying: Boolean,
    onPreviousClick: () -> Unit,
    onRewindClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onFastForwardClick: () -> Unit,
    onNextClick: () -> Unit,
    onShareClick: () -> Unit,
    onPlaylistClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            DownloadButton(
                onDownloadClick = onDownloadClick,
                isDownload = isDownload,
                isConnected = isConnected,
                downloadInfo = downloadInfo
            )
            AddButton(
                onAddClick = onAddClick,
                expanded = expanded,
                onDropDownMenuDismissRequestClick = onDropDownMenuDismissRequestClick,
                userPlaylistResponses = userPlaylistResponses,
                onAddNewClick = onAddNewClick,
                onMenuItemClick = onMenuItemClick,
                showDialog = showDialog,
                newPlaylistName = newPlaylistName,
                onAlertDialogDismissRequest = onAlertDialogDismissRequest,
                onConfirmClick = onConfirmClick,
                onDismissClick = onDismissClick,
                onAlertTextValueChange = onAlertTextValueChange
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        PlayerSlider(
            sliderPosition = sliderPosition,
            sliderDuration = sliderDuration,
            onSliderValueChange = onSliderValueChange,
            currentPosition = currentPosition,
            duration = duration
        )

        Spacer(modifier = Modifier.height(24.dp))

        PlayerControlButtons(
            isPlaying = isPlaying,
            onPreviousClick = onPreviousClick,
            onRewindClick = onRewindClick,
            onPlayPauseClick = onPlayPauseClick,
            onFastForwardClick = onFastForwardClick,
            onNextClick = onNextClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        ShareAndPlaylistButtons(
            onShareClick = onShareClick,
            onPlaylistClick = onPlaylistClick
        )
    }
}

@Composable
fun DownloadButton(
    onDownloadClick: () -> Unit,
    isDownload: Boolean,
    isConnected: Boolean,
    downloadInfo: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = { onDownloadClick() },
            enabled = if (!isDownload) isConnected else true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = "Download",
                tint = if (isDownload) {
                    Color.Green
                } else {
                    if (isConnected) {
                        Color.Black
                    } else {
                        Color.Gray
                    }
                },
                modifier = Modifier.size(32.dp)
            )
        }
        if (downloadInfo.isNotEmpty()) {
            Text(text = downloadInfo)
        }
    }
}

@Composable
fun AddButton(
    onAddClick: () -> Unit,
    expanded: Boolean,
    onDropDownMenuDismissRequestClick: () -> Unit,
    userPlaylistResponses: List<UserPlaylistResponse>,
    onAddNewClick: () -> Unit,
    onMenuItemClick: (UserPlaylistResponse) -> Unit,
    showDialog: Boolean,
    newPlaylistName: String,
    onAlertDialogDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    onAlertTextValueChange: (String) -> Unit
) {
    Box {
        IconButton(
            onClick = { onAddClick() }
        ) {
            Icon(
                Icons.Default.AddCircle,
                contentDescription = "Add",
                tint = Color.Black,
                modifier = Modifier.size(34.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDropDownMenuDismissRequestClick() }, //expanded = false
            modifier = if (userPlaylistResponses.size < 10) {
                Modifier.background(color = Color.LightGray)
            } else {
                Modifier
                    .background(color = Color.LightGray)
                    .fillMaxHeight(0.5f)
            }
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add New",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Add, contentDescription = null
                        )
                    }
                },
                onClick = { onAddNewClick() }
            )
            userPlaylistResponses.forEach { userPlaylistResponse ->
                DropdownMenuItem(
                    text = {
                        Text(text = userPlaylistResponse.userPlaylistName)
                    },
                    onClick = { onMenuItemClick(userPlaylistResponse) }
                )
            }
        }
        if (showDialog) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { onAlertDialogDismissRequest() },
                title = { Text(text = "Add New Playlist") }, text = {
                    Column {
                        TextField(
                            value = newPlaylistName,
                            onValueChange = { onAlertTextValueChange(it) },
                            label = { Text("Playlist Name") },
                            colors = TextFieldDefaults.colors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.Gray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedSuffixColor = Color.Black,
                                selectionColors = TextSelectionColors(
                                    Color.Black, Color.Gray
                                ),
                                focusedIndicatorColor = Color.Black
                            )
                        )
                    }
                }, confirmButton = {
                    Button(
                        onClick = { onConfirmClick() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text("OK")
                    }
                }, dismissButton = {
                    Button(
                        onClick = { onDismissClick() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text("Cancel")
                    }
                })
        }
    }
}

@Composable
fun PlayerSlider(
    sliderPosition: Float,
    sliderDuration: Float,
    onSliderValueChange: (Float) -> Unit,
    currentPosition: Long,
    duration: Long
) {
    Slider(
        value = sliderPosition, onValueChange = { newValue ->
            onSliderValueChange(newValue)
        },
        valueRange = 0f..sliderDuration, colors = SliderDefaults.colors(
            thumbColor = Color.Black,
            inactiveTrackColor = Color.Gray,
            activeTrackColor = Color.Black
        ),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = formatTime(currentPosition))
        Text(text = formatTime(duration))
    }
}

@Composable
fun PlayerControlButtons(
    isPlaying: Boolean,
    onPreviousClick: () -> Unit,
    onRewindClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onFastForwardClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { onPreviousClick() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_previous),
                contentDescription = "Previous",
                modifier = Modifier.size(200.dp)
            )
        }
        IconButton(onClick = { onRewindClick() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_backward_10),
                contentDescription = "Rewind 10s",
                modifier = Modifier.size(200.dp)
            )
        }
        IconButton(onClick = { onPlayPauseClick() }) {
            Icon(
                painter = if (isPlaying) painterResource(id = R.drawable.ic_pause) else painterResource(
                    id = R.drawable.ic_play
                ),
                contentDescription = "Play/Pause", modifier = Modifier.size(200.dp)
            )
        }
        IconButton(onClick = { onFastForwardClick() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_forward_10),
                contentDescription = "Forward 10s",
                modifier = Modifier.size(200.dp)
            )
        }
        IconButton(onClick = { onNextClick() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_next),
                contentDescription = "Next",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Composable
fun ShareAndPlaylistButtons(
    onShareClick: () -> Unit,
    onPlaylistClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onShareClick() }) {
            Icon(
                Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(32.dp)
            )
        }
        IconButton(onClick = { onPlaylistClick() }) {
            Icon(
                Icons.Default.Menu, contentDescription = "Playlist", modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun PlayerTrackList(
    playerList: List<PlayerTrack>, onClick: (PlayerTrack) -> Unit, currentTrackId: String
) {
    Column(
        modifier = Modifier.background(color = Color.White)
    ) {
        Text(
            text = "Current Playlist",
            style = MaterialTheme.typography.labelLarge,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(color = Color.White)
        )
        LazyColumn(
            modifier = Modifier
                .background(color = Color.White)
                .padding(bottom = 64.dp)
        ) {
            items(playerList) { playerTrack ->
                PlayerTrackItem(
                    playerTrack = playerTrack,
                    onClick = onClick,
                    isCurrentTrack = playerTrack.trackId == currentTrackId
                )
            }
        }
    }
}

@Composable
fun PlayerTrackItem(
    playerTrack: PlayerTrack, onClick: (PlayerTrack) -> Unit, isCurrentTrack: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(playerTrack) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = playerTrack.trackImage,
                error = painterResource(id = R.drawable.ic_play),
                fallback = painterResource(id = R.drawable.ic_play)
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(64.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = playerTrack.trackTitle,
                color = if (isCurrentTrack) {
                    Color.Green
                } else {
                    Color.Black
                },
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(200.dp)
            )
            Text(
                text = playerTrack.trackArtistName,
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}


@SuppressLint("DefaultLocale")
fun formatTime(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    return String.format("%d:%02d", minutes, seconds)
}