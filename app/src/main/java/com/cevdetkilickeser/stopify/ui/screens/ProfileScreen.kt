package com.cevdetkilickeser.stopify.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(navController: NavController) {
    Column {
        Button(onClick = { FirebaseAuth.getInstance().signOut()
        navController.navigate("login")
        })
        {}
    }

}