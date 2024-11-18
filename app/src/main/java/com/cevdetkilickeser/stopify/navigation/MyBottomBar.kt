package com.cevdetkilickeser.stopify.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color


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