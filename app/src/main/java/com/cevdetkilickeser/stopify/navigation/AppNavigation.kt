package com.cevdetkilickeser.stopify.navigation

import android.app.Activity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
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
