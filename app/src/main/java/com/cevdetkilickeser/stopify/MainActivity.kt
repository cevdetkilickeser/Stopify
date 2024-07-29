package com.cevdetkilickeser.stopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cevdetkilickeser.stopify.ui.home.HomeScreen
import com.cevdetkilickeser.stopify.ui.theme.StopifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopifyTheme {
                HomeScreen(viewModel = hiltViewModel())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StopifyTheme {

    }
}