package com.cevdetkilickeser.stopify.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cevdetkilickeser.stopify.ui.home.HomeScreen
import com.cevdetkilickeser.stopify.ui.single_genre.SingleGenreScreen

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable(
            "single_genre/{genreId}",
            arguments = listOf(navArgument("genreId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val genreId = navBackStackEntry.arguments?.getString("genreId") ?: return@composable
            SingleGenreScreen(genreId)
        }
    }
}