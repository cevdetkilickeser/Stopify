package com.cevdetkilickeser.stopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.cevdetkilickeser.stopify.ui.MainScreen
import com.cevdetkilickeser.stopify.ui.theme.StopifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            StopifyTheme {
                val statusBarColor = Color.Black

                SideEffect {
                    window.statusBarColor = statusBarColor.toArgb()
                }
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
}