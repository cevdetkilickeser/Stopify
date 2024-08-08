package com.cevdetkilickeser.stopify.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cevdetkilickeser.stopify.ui.screens.AlbumScreen
import com.cevdetkilickeser.stopify.ui.screens.ArtistScreen
import com.cevdetkilickeser.stopify.ui.screens.HomeScreen
import com.cevdetkilickeser.stopify.ui.screens.LikesScreen
import com.cevdetkilickeser.stopify.ui.screens.LoginScreen
import com.cevdetkilickeser.stopify.ui.screens.MusicPlayerScreen
import com.cevdetkilickeser.stopify.ui.screens.PlaylistScreen
import com.cevdetkilickeser.stopify.ui.screens.SearchScreen
import com.cevdetkilickeser.stopify.ui.screens.SignupScreen
import com.cevdetkilickeser.stopify.ui.screens.SingleGenreScreen
import com.cevdetkilickeser.stopify.ui.screens.SplashScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route
    val userId = FirebaseAuth.getInstance().currentUser?.email.toString()
    val context = LocalContext.current as? Activity

    Scaffold(
        bottomBar = {
            if (bottomAppBarVisible.value) {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentDestination == "home",
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.LightGray
                        ),
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
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.LightGray
                        ),
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
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.LightGray
                        ),
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
                    AlbumScreen(navController = navController, albumId = albumId, userId = userId)
                }
                composable(
                    "player/{player_track}",
                    arguments = listOf(
                        navArgument("player_track") { type = NavType.StringType })
                ) { navBackStackEntry ->
                    val playerTrackGson =
                        navBackStackEntry.arguments?.getString("player_track") ?: return@composable
                    val playerTrack = Gson().fromJson(playerTrackGson, PlayerTrack::class.java)
                    MusicPlayerScreen(
                        playerTrack = playerTrack
                    )
                }
            }
        }
    )
}
