package com.cevdetkilickeser.stopify.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cevdetkilickeser.stopify.data.model.player.PlayerTrack
import com.cevdetkilickeser.stopify.json
import com.cevdetkilickeser.stopify.ui.album.AlbumScreen
import com.cevdetkilickeser.stopify.ui.artist.ArtistScreen
import com.cevdetkilickeser.stopify.ui.downloads.DownloadsScreen
import com.cevdetkilickeser.stopify.ui.home.HomeScreen
import com.cevdetkilickeser.stopify.ui.likes.LikesScreen
import com.cevdetkilickeser.stopify.ui.login.LoginScreen
import com.cevdetkilickeser.stopify.ui.musicplayer.MusicPlayerScreen
import com.cevdetkilickeser.stopify.ui.playlist.PlaylistScreen
import com.cevdetkilickeser.stopify.ui.profile.ProfileScreen
import com.cevdetkilickeser.stopify.ui.search.SearchScreen
import com.cevdetkilickeser.stopify.ui.signup.SignupScreen
import com.cevdetkilickeser.stopify.ui.singlegenre.SingleGenreScreen
import com.cevdetkilickeser.stopify.ui.userplaylist.UserPlayListScreen


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