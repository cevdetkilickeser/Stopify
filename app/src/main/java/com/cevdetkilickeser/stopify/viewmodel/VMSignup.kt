package com.cevdetkilickeser.stopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMSignup @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _signupState = MutableStateFlow(false)
    val signupState: StateFlow<Boolean> = _signupState

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        _signupState.value = task.isSuccessful
                    }
            } catch (e: Exception) {
                _signupState.value = false
            }
        }
    }
}