package com.cevdetkilickeser.stopify.ui.signup

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

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loadingState.value =  true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        _signupState.value = task.isSuccessful
                    }.addOnFailureListener { error ->
                        _errorState.value = error.message
                        _loadingState.value = false
                    }
            } catch (e: Exception) {
                _signupState.value = false
            }
        }
    }
}