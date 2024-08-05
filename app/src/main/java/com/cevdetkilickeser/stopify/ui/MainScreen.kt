package com.cevdetkilickeser.stopify.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cevdetkilickeser.stopify.data.playlist.Track
import com.cevdetkilickeser.stopify.ui.album.AlbumScreen
import com.cevdetkilickeser.stopify.ui.artist.ArtistScreen
import com.cevdetkilickeser.stopify.ui.home.HomeScreen
import com.cevdetkilickeser.stopify.ui.likes.LikesScreen
import com.cevdetkilickeser.stopify.ui.login.LoginScreen
import com.cevdetkilickeser.stopify.ui.player.MusicPlayerScreen
import com.cevdetkilickeser.stopify.ui.playlist.PlaylistScreen
import com.cevdetkilickeser.stopify.ui.search.SearchScreen
import com.cevdetkilickeser.stopify.ui.signup.SignupScreen
import com.cevdetkilickeser.stopify.ui.single_genre.SingleGenreScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

@Composable
fun MainScreen() {
    val userId: String = FirebaseAuth.getInstance().currentUser!!.toString()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route
    val bottomAppBarVisible = remember { mutableStateOf(false) }

    LaunchedEffect(currentDestination) {
        bottomAppBarVisible.value =
            !(currentDestination == "splash" || currentDestination == "login" || currentDestination == "signup")
    }

    Scaffold(
        bottomBar = {
            if (bottomAppBarVisible.value) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentDestination == "home",
                        onClick = {
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        label = { Text("Search") },
                        selected = currentDestination == "search",
                        onClick = {
                            navController.navigate("search") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Likes") },
                        label = { Text("Likes") },
                        selected = currentDestination == "likes",
                        onClick = {
                            navController.navigate("likes") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "splash",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("splash") {
                    SplashScreen(navController)
                }
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
                    SearchScreen(navController, userId)
                }
                composable("likes") {
                    LikesScreen(navController, userId)
                }
                composable(
                    "single_genre/{genreId}",
                    arguments = listOf(navArgument("genreId") { type = NavType.StringType })
                ) { navBackStackEntry ->
                    val genreId =
                        navBackStackEntry.arguments?.getString("genreId") ?: return@composable
                    SingleGenreScreen(navController, genreId)
                }
                composable(
                    "playlist/{playlistId}",
                    arguments = listOf(navArgument("playlistId") { type = NavType.StringType })
                ) { navBackStackEntry ->
                    val playlistId =
                        navBackStackEntry.arguments?.getString("playlistId") ?: return@composable
                    PlaylistScreen(navController, playlistId, userId)
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
                    AlbumScreen(navController = navController, albumId = albumId)
                }
                composable(
                    "player/{trackJson}",
                    arguments = listOf(navArgument("trackJson") { type = NavType.StringType })
                ) { navBackStackEntry ->
                    val trackJson =
                        navBackStackEntry.arguments?.getString("trackJson") ?: return@composable
                    val gson = Gson()
                    val track = gson.fromJson(trackJson, Track::class.java)
                    MusicPlayerScreen(track)
                }
            }
        }
    )
}
