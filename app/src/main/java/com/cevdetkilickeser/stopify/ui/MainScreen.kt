package com.cevdetkilickeser.stopify.ui

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.cevdetkilickeser.stopify.R
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.json
import com.cevdetkilickeser.stopify.ui.screens.AlbumScreen
import com.cevdetkilickeser.stopify.ui.screens.ArtistScreen
import com.cevdetkilickeser.stopify.ui.screens.DownloadsScreen
import com.cevdetkilickeser.stopify.ui.screens.HomeScreen
import com.cevdetkilickeser.stopify.ui.screens.LikesScreen
import com.cevdetkilickeser.stopify.ui.screens.LoginScreen
import com.cevdetkilickeser.stopify.ui.screens.MusicPlayerScreen
import com.cevdetkilickeser.stopify.ui.screens.PlaylistScreen
import com.cevdetkilickeser.stopify.ui.screens.ProfileScreen
import com.cevdetkilickeser.stopify.ui.screens.SearchScreen
import com.cevdetkilickeser.stopify.ui.screens.SignupScreen
import com.cevdetkilickeser.stopify.ui.screens.SingleGenreScreen
import com.cevdetkilickeser.stopify.ui.screens.UserPlayListScreen
import com.cevdetkilickeser.stopify.viewmodel.VMMain

@Composable
fun MainScreen (navController: NavHostController, viewModel: VMMain = hiltViewModel()) {
    val activity = LocalContext.current as? Activity
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route
    val userId: String? = viewModel.auth.currentUser?.email

    Scaffold(
        containerColor = Color.White,
        topBar = {
            MyTopAppBar(
                currentDestination = currentDestination,
                onBackClick = {
                    if (currentDestination == "home") {
                        activity?.finish()
                    } else {
                        navController.popBackStack()
                    }
                },
                onProfileClick = {
                    navController.navigate("profile") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        bottomBar = {
            MyBottomBar(
                currentDestination = currentDestination,
                onHomeClick = {
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSearchClick = {
                    navController.navigate("search") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onLikesClick = {
                    navController.navigate("likes") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        },
        content = { innerPadding ->
            MyNavHost(navController = navController, userId = userId, innerPadding = innerPadding)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(currentDestination: String?, onBackClick: () -> Unit, onProfileClick: () -> Unit) {
    val topAppBarVisible = remember { mutableStateOf(false) }
    val routes = arrayOf("splash", "login", "signup", "player")
    topAppBarVisible.value =
        if (currentDestination == null) {
            false
        } else {
            !(routes.any { route -> currentDestination.contains(route) })
        }

    if (topAppBarVisible.value) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_app_icon_foreground), contentDescription = null, tint = Color.Red, modifier = Modifier.size(48.dp))
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black
            )
        )
    }
}

@Composable
fun MyBottomBar(
    currentDestination: String?,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onLikesClick: () -> Unit
) {
    val bottomAppBarVisible = remember { mutableStateOf(false) }
    val routes = arrayOf("splash", "login", "signup", "player")
    bottomAppBarVisible.value =
        if (currentDestination == null) {
            false
        } else {
            !(routes.any { route -> currentDestination.contains(route) })
        }

    if (bottomAppBarVisible.value) {
        NavigationBar(containerColor = Color.White) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = currentDestination == "home",
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray
                ),
                onClick = { onHomeClick() }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                label = { Text("Search") },
                selected = currentDestination == "search",
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray
                ),
                onClick = { onSearchClick() }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Favorite, contentDescription = "Likes") },
                label = { Text("Likes") },
                selected = currentDestination == "likes",
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray
                ),
                onClick = { onLikesClick() }
            )
        }
    }
}

@Composable
fun MyNavHost(navController: NavHostController, userId: String?, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = if (userId.isNullOrEmpty()) {
            "login"
        } else {
            "home"
        },
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("search") {
            SearchScreen(navController, userId!!)
        }
        composable("likes") {
            LikesScreen(navController, userId!!)
        }
        composable("profile") {
            ProfileScreen(navController, userId!!)
        }
        composable(
            "single_genre/{genreId}/{genreName}",
            arguments = listOf(
                navArgument("genreId") { type = NavType.StringType },
                navArgument("genreName") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val genreId =
                navBackStackEntry.arguments?.getString("genreId") ?: return@composable
            val genreName =
                navBackStackEntry.arguments?.getString("genreName") ?: return@composable
            SingleGenreScreen(navController, genreId, genreName)
        }
        composable(
            "playlist/{genreName}/{playlistId}/{playlistName}",
            arguments = listOf(
                navArgument("genreName") { type = NavType.StringType },
                navArgument("playlistId") { type = NavType.StringType },
                navArgument("playlistName") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val genreName =
                navBackStackEntry.arguments?.getString("genreName") ?: return@composable
            val playlistId =
                navBackStackEntry.arguments?.getString("playlistId") ?: return@composable
            val playlistName =
                navBackStackEntry.arguments?.getString("playlistName") ?: return@composable
            PlaylistScreen(navController, playlistId, genreName, playlistName, userId!!)
        }
        composable(
            "artist/{artistId}",
            arguments = listOf(navArgument("artistId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val artistId =
                navBackStackEntry.arguments?.getString("artistId") ?: return@composable
            ArtistScreen(navController = navController, artistId = artistId)
        }
        composable(
            "album/{albumId}",
            arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val albumId =
                navBackStackEntry.arguments?.getString("albumId") ?: return@composable
            AlbumScreen(navController = navController, albumId = albumId, userId = userId!!)
        }
        composable(
            "player/{startIndex}/{playerTrackList}",
            arguments = listOf(
                navArgument("startIndex") { type = NavType.IntType },
                navArgument("playerTrackList") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val startIndex =
                navBackStackEntry.arguments?.getInt("startIndex") ?: return@composable
            val playerTrackListJson =
                navBackStackEntry.arguments?.getString("playerTrackList") ?: return@composable
            val playerTrackList = json.decodeFromString<List<PlayerTrack>>(playerTrackListJson)
            MusicPlayerScreen(startIndex = startIndex, playerTrackList = playerTrackList, userId = userId!!)
        }
        composable("downloads") {
            DownloadsScreen(navController = navController, userId = userId!!)
        }
        composable(
            "userPlaylist/{userPlaylistId}/{userPlaylistName}",
            arguments = listOf(
                navArgument("userPlaylistId") { type = NavType.IntType },
                navArgument("userPlaylistName") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val userPlaylistId =
                navBackStackEntry.arguments?.getInt("userPlaylistId") ?: return@composable
            val userPlaylistName =
                navBackStackEntry.arguments?.getString("userPlaylistName") ?: return@composable
            UserPlayListScreen(navController = navController, userId = userId!!, userPlaylistId = userPlaylistId, userPlaylistName = userPlaylistName)
        }
    }
}
