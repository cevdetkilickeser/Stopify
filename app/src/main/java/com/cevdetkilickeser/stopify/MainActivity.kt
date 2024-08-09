package com.cevdetkilickeser.stopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.cevdetkilickeser.stopify.ui.MainScreen
import com.cevdetkilickeser.stopify.ui.theme.StopifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        networkMonitor = NetworkMonitor(this)
        setContent {
            StopifyTheme {
                val statusBarColor = Color.Black

                SideEffect {
                    window.statusBarColor = statusBarColor.toArgb()
                }
                val navController = rememberNavController()
                MainScreen(navController, networkMonitor)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.startNetworkCallback(
            onNetworkAvailable = {  },
            onNetworkLost = {  }
        )
    }

    override fun onPause() {
        super.onPause()
        networkMonitor.stopNetworkCallback()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StopifyTheme {

    }
}