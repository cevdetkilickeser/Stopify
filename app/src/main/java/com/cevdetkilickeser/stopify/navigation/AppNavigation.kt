package com.cevdetkilickeser.stopify.navigation

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation (
    navController: NavHostController
) {
    val activity = LocalContext.current as? Activity
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route
    val userId: String? = FirebaseAuth.getInstance().currentUser?.email

    Scaffold(
        containerColor = Color.White,
        topBar = {
            MyTopBar(
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
                        popUpTo("profile") {
                            inclusive = false
                        }
                        restoreState = true
                        launchSingleTop = true
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
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onSearchClick = {
                    navController.navigate("search") {
                        popUpTo("search") {
                            inclusive = false
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onLikesClick = {
                    navController.navigate("likes") {
                        popUpTo("likes") {
                            inclusive = false
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        },
        content = { innerPadding ->
            MyNavHost(navController = navController, userId = userId, innerPadding = innerPadding)
        }
    )
}
