package com.cevdetkilickeser.stopify

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cevdetkilickeser.stopify.ui.MainScreen
import com.cevdetkilickeser.stopify.ui.theme.StopifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        installSplashScreen()
        setContent {
            StopifyTheme {
                val statusBarColor = Color.Black

                SideEffect {
                    window.statusBarColor = statusBarColor.toArgb()
                }
                MainScreen()
            }
        }
    }
}